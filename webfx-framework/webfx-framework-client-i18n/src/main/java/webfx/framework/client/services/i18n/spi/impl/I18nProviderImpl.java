package webfx.framework.client.services.i18n.spi.impl;

import javafx.beans.property.*;
import javafx.beans.value.ObservableObjectValue;
import javafx.beans.value.ObservableStringValue;
import webfx.framework.client.services.i18n.Dictionary;
import webfx.framework.client.services.i18n.TranslationPart;
import webfx.framework.client.services.i18n.spi.I18nProvider;
import webfx.platform.client.services.uischeduler.UiScheduler;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.*;

/**
 * @author Bruno Salmon
 */
public class I18nProviderImpl implements I18nProvider {

    private final Map<Object/*i18nKey*/, Map<TranslationPart, Reference<StringProperty>>> translations = new HashMap<>();
    private final Object defaultLanguage; // The language to find message parts (such as graphic) when missing in the current language
    private boolean dictionaryLoadRequired;
    private final DictionaryLoader dictionaryLoader;
    private Set<Object> unloadedKeys, unloadedDefaultKeys;

    public I18nProviderImpl(DictionaryLoader dictionaryLoader, Object defaultLanguage, Object initialLanguage) {
        this.dictionaryLoader = dictionaryLoader;
        if (defaultLanguage == null)
            defaultLanguage = guessDefaultLanguage();
        if (defaultLanguage == null) {
            if (initialLanguage != null)
                defaultLanguage = initialLanguage;
            else
                throw new IllegalArgumentException("No default/initial language set for I18n initialization");
        }
        this.defaultLanguage = defaultLanguage;
        languageProperty.addListener((observable, oldValue, newValue) -> onLanguageChanged());
        if (initialLanguage == null)
            initialLanguage = guessInitialLanguage();
        if (initialLanguage == null)
            initialLanguage = defaultLanguage;
        setLanguage(initialLanguage);
    }

    private Object guessDefaultLanguage() {
        return getSupportedLanguages().stream().findFirst().orElse(null);
    }

    private Object guessInitialLanguage() {
        return null;
    }

    private final ObjectProperty<Object> languageProperty = new SimpleObjectProperty<>();
    @Override
    public ObjectProperty<Object> languageProperty() {
        return languageProperty;
    }

    private final ObjectProperty<Dictionary> dictionaryProperty = new SimpleObjectProperty<>();
    @Override
    public ObservableObjectValue<Dictionary> dictionaryProperty() {
        return dictionaryProperty;
    }

    @Override
    public Object getDefaultLanguage() {
        return defaultLanguage;
    }

    private final Property<Dictionary> defaultDictionaryProperty = new SimpleObjectProperty<>();

    @Override
    public Dictionary getDefaultDictionary() {
        return defaultDictionaryProperty.getValue();
    }

    @Override
    public ObservableStringValue observablePart(Object i18nKey, TranslationPart part) {
        StringProperty translationPartProperty = getTranslationPartProperty(i18nKey, part);
        if (translationPartProperty == null)
            getMessageMap(i18nKey, true).put(part, new WeakReference<>(translationPartProperty = createTranslationPartProperty(i18nKey, part)));
        return translationPartProperty;
    }

    private Map<TranslationPart, Reference<StringProperty>> getMessageMap(Object i18nKey, boolean createIfNotExists) {
        Map<TranslationPart, Reference<StringProperty>> messageMap = translations.get(i18nKey);
        if (messageMap == null && createIfNotExists)
            synchronized (translations) {
                translations.put(i18nKey, messageMap = new HashMap<>());
            }
        return messageMap;
    }

    private StringProperty getTranslationPartProperty(Object i18nKey, TranslationPart part) {
        Map<TranslationPart, Reference<StringProperty>> messageMap = getMessageMap(i18nKey, false);
        if (messageMap == null)
            return null;
        Reference<StringProperty> ref = messageMap.get(part);
        return ref == null ? null : ref.get();
    }

    private StringProperty createTranslationPartProperty(Object i18nKey, TranslationPart part) {
        return refreshTranslationPart(new SimpleStringProperty(), i18nKey, part);
    }

    private void onLanguageChanged() {
        dictionaryLoadRequired = true;
        refreshAllTranslationParts();
    }

    private synchronized void refreshAllTranslationParts() {
        synchronized (translations) {
            // We iterate through the translation map to update all parts (text, graphic, etc...) of all messages (i18nKey)
            for (Iterator<Map.Entry<Object, Map<TranslationPart, Reference<StringProperty>>>> it = translations.entrySet().iterator(); it.hasNext(); ) {
                Map.Entry<Object, Map<TranslationPart, Reference<StringProperty>>> messageMapEntry = it.next();
                Object i18nKey = messageMapEntry.getKey();
                Map<TranslationPart, Reference<StringProperty>> messageMap = messageMapEntry.getValue();
                for (Iterator<Map.Entry<TranslationPart, Reference<StringProperty>>> it2 = messageMap.entrySet().iterator(); it2.hasNext(); ) {
                    Map.Entry<TranslationPart, Reference<StringProperty>> translationPartEntry = it2.next();
                    // Getting the translationPartProperty through the reference
                    Reference<StringProperty> value = translationPartEntry.getValue();
                    StringProperty translationPartProperty = value == null ? null : value.get();
                    // Although a translationPartProperty is never null at initialization, it can be dropped by the GC since
                    // it is contained in a WeakReference. If this happens, this means that the client software actually
                    // doesn't use it (either never from the beginning or just not anymore after an activity is closed
                    // for example), so we can just remove that entry to release some memory.
                    if (translationPartProperty == null) // Means the client software doesn't use this i18nKey,translationPart pair
                        it2.remove(); // So we can drop this entry
                    else // Otherwise, the client software still uses it so we need to update it
                        refreshTranslationPart(translationPartProperty, i18nKey, translationPartEntry.getKey());
                }
                // Although a message map is never empty at initialization, it can become empty if all i18nKey,translationPart
                // have been removed (as explained above). If this happens, this means that the client software actually
                // doesn't use this message at all (either never from the beginning or not anymore).
                if (messageMap.isEmpty()) // Means the client software doesn't use this i18nKey message
                    it.remove(); // So we can drop this entry
            }
        }
    }

    private StringProperty refreshTranslationPart(StringProperty translationPartProperty, Object i18nKey, TranslationPart part) {
        if (dictionaryLoadRequired && dictionaryLoader != null)
            scheduleMessageLoading(i18nKey, false);
        else
            translationPartProperty.setValue(instantTranslatePart(i18nKey, part));
        return translationPartProperty;
    }

    @Override
    public void scheduleMessageLoading(Object i18nKey, boolean inDefaultLanguage) {
        Set<Object> unloadedKeys = getUnloadedKeys(inDefaultLanguage);
        if (unloadedKeys != null)
            unloadedKeys.add(i18nKey);
        else {
            setUnloadedKeys(unloadedKeys = new HashSet<>(), inDefaultLanguage);
            unloadedKeys.add(i18nKey);
            UiScheduler.scheduleDeferred(() -> {
                Object language = inDefaultLanguage ? getDefaultLanguage() : getLanguage();
                dictionaryLoader.loadDictionary(language, getUnloadedKeys(inDefaultLanguage)).setHandler(asyncResult -> {
                    if (!inDefaultLanguage)
                        dictionaryProperty.setValue(asyncResult.result());
                    if (language.equals(getDefaultLanguage()))
                        defaultDictionaryProperty.setValue(asyncResult.result());
                    dictionaryLoadRequired = false;
                    refreshAllTranslationParts();
                });
                setUnloadedKeys(null, inDefaultLanguage);
            });
        }
    }

    private Set<Object> getUnloadedKeys(boolean inDefaultLanguage) {
        return inDefaultLanguage ? unloadedDefaultKeys : unloadedKeys;
    }

    private void setUnloadedKeys(Set<Object> unloadedKeys, boolean inDefaultLanguage) {
        if (inDefaultLanguage)
            unloadedDefaultKeys = unloadedKeys;
        else
            this.unloadedKeys = unloadedKeys;
    }
}
