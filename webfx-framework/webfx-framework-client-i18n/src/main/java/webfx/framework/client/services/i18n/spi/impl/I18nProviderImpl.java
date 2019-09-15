package webfx.framework.client.services.i18n.spi.impl;

import javafx.beans.property.*;
import javafx.beans.value.ObservableObjectValue;
import javafx.beans.value.ObservableStringValue;
import webfx.framework.client.services.i18n.Dictionary;
import webfx.framework.client.services.i18n.I18nPart;
import webfx.framework.client.services.i18n.spi.HasDictionaryMessageKey;
import webfx.framework.client.services.i18n.spi.I18nProvider;
import webfx.platform.client.services.uischeduler.UiScheduler;
import webfx.platform.shared.util.Strings;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.*;

/**
 * @author Bruno Salmon
 */
public class I18nProviderImpl implements I18nProvider {

    private final Map<Object/*i18nKey*/, Map<I18nPart, Reference<StringProperty>>> translations = new HashMap<>();
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
    public ObservableStringValue i18nPartProperty(Object i18nKey, I18nPart part) {
        StringProperty i18nPartProperty = getI18nPartProperty(i18nKey, part);
        if (i18nPartProperty == null)
            getMessageMap(i18nKey, true).put(part, new WeakReference<>(i18nPartProperty = createI18nPartProperty(i18nKey, part)));
        return i18nPartProperty;
    }

    private Map<I18nPart, Reference<StringProperty>> getMessageMap(Object i18nKey, boolean createIfNotExists) {
        Map<I18nPart, Reference<StringProperty>> messageMap = translations.get(i18nKey);
        if (messageMap == null && createIfNotExists)
            synchronized (translations) {
                translations.put(i18nKey, messageMap = new HashMap<>());
            }
        return messageMap;
    }

    private StringProperty getI18nPartProperty(Object i18nKey, I18nPart part) {
        Map<I18nPart, Reference<StringProperty>> messageMap = getMessageMap(i18nKey, false);
        if (messageMap == null)
            return null;
        Reference<StringProperty> ref = messageMap.get(part);
        return ref == null ? null : ref.get();
    }

    private StringProperty createI18nPartProperty(Object i18nKey, I18nPart part) {
        return refreshI18nPart(new SimpleStringProperty(), i18nKey, part);
    }

    public String getI18nPartValue(Object i18nKey, I18nPart part) {
        return getI18nPartValue(i18nKey, part, false);
    }

    private String getI18nPartValue(Object i18nKey, I18nPart part, boolean skipPrefixOrSuffix) {
        Dictionary dictionary = getDictionary();
        String partTranslation = getI18nPartValue(i18nKey, part, dictionary, skipPrefixOrSuffix);
        if (partTranslation == null) {
            Dictionary defaultDictionary = getDefaultDictionary();
            if (dictionary != defaultDictionary && defaultDictionary != null)
                partTranslation = getI18nPartValue(i18nKey, part, defaultDictionary, skipPrefixOrSuffix);
            if (partTranslation == null) {
                scheduleMessageLoading(i18nKey, true);
                if (part == I18nPart.TEXT)
                    partTranslation = whatToReturnWhenI18nTextIsNotFound(i18nKey);
            }
        }
        return partTranslation;
    }

    private String interpretDictionaryValue(Object i18nKey, I18nPart part, String value) {
        while (value != null && value.startsWith("[") && value.endsWith("]")) {
            String token = value.substring(1, value.length() - 1);
            String tokenValue = interpretToken(i18nKey, part, token);
            /*if (token.equals(tokenValue))
                break;*/
            value = tokenValue;
        }
        return value;
    }

    protected String interpretToken(Object i18nKey, I18nPart part, String token) {
        return getI18nPartValue(new I18nSubKey(token, i18nKey), part);
    }

    private String getI18nPartValue(Object i18nKey, I18nPart part, Dictionary dictionary, boolean skipPrefixOrSuffix) {
        String partTranslation = null;
        if (dictionary != null && i18nKey != null) {
            Object dictionaryMessageKey = i18nKeyToDictionaryMessageKey(i18nKey);
            partTranslation = dictionary.getI18nPartValue(dictionaryMessageKey, part);
            if (skipPrefixOrSuffix && partTranslation != null)
                partTranslation = interpretDictionaryValue(i18nKey, part, partTranslation);
            else if (partTranslation == null && !skipPrefixOrSuffix) {
                String sKey = Strings.asString(dictionaryMessageKey);
                int length = Strings.length(sKey);
                if (length > 1) {
                    int index = 0;
                    while (index < length && !Character.isLetterOrDigit(sKey.charAt(index)))
                        index++;
                    if (index > 0) {
                        String prefix = sKey.substring(0, index);
                        switch (prefix) {
                            case "<<":
                                partTranslation = getI18nPartValue(new I18nSubKey(sKey.substring(prefix.length(), length), i18nKey), part, dictionary, true);
                                if (partTranslation != null && part == I18nPart.TEXT)
                                    partTranslation = getI18nPartValue(prefix, part, true) + partTranslation;
                        }
                    }
                }
                if (partTranslation == null && length > 1) {
                    int index = length;
                    while (index > 0 && !Character.isLetterOrDigit(sKey.charAt(index - 1)))
                        index--;
                    if (index < length) {
                        String suffix = sKey.substring(index, length);
                        switch (suffix) {
                            case ":":
                            case "?":
                            case ">>":
                            case "...":
                                partTranslation = getI18nPartValue(new I18nSubKey(sKey.substring(0, length - suffix.length()), i18nKey), part, dictionary, true);
                                if (partTranslation != null && part == I18nPart.TEXT)
                                    partTranslation = partTranslation + getI18nPartValue(suffix, part, true);
                        }
                    }
                }
            }
        }
        return partTranslation;
    }

    private Object i18nKeyToDictionaryMessageKey(Object i18nKey) {
        if (i18nKey instanceof HasDictionaryMessageKey)
            return ((HasDictionaryMessageKey) i18nKey).getDictionaryMessageKey();
        return i18nKey;
    }

    private String whatToReturnWhenI18nTextIsNotFound(Object i18nKey) {
        return Strings.toString(i18nKeyToDictionaryMessageKey(i18nKey));
    }

    private void onLanguageChanged() {
        dictionaryLoadRequired = true;
        refreshAllTranslations();
    }

    private synchronized void refreshAllTranslations() {
        synchronized (translations) {
            // We iterate through the translation map to update all parts (text, graphic, etc...) of all messages (i18nKey)
            for (Iterator<Map.Entry<Object, Map<I18nPart, Reference<StringProperty>>>> it = translations.entrySet().iterator(); it.hasNext(); ) {
                Map.Entry<Object, Map<I18nPart, Reference<StringProperty>>> messageMapEntry = it.next();
                refreshMessageTranslations(messageMapEntry.getKey(), messageMapEntry.getValue());
                // Although a message map is never empty at initialization, it can become empty if all i18nKey,translationPart
                // have been removed (as explained above). If this happens, this means that the client software actually
                // doesn't use this message at all (either never from the beginning or not anymore).
                if (messageMapEntry.getValue().isEmpty()) // Means the client software doesn't use this i18nKey message
                    it.remove(); // So we can drop this entry
            }
        }
    }

    public void refreshMessageTranslations(Object i18nKey) {
        refreshMessageTranslations(i18nKey, translations.get(i18nKey));
    }

    private void refreshMessageTranslations(Object i18nKey, Map<I18nPart, Reference<StringProperty>> messageMap) {
        if (messageMap != null)
            for (Iterator<Map.Entry<I18nPart, Reference<StringProperty>>> it = messageMap.entrySet().iterator(); it.hasNext(); ) {
                Map.Entry<I18nPart, Reference<StringProperty>> translationPartEntry = it.next();
                // Getting the i18nPartProperty through the reference
                Reference<StringProperty> value = translationPartEntry.getValue();
                StringProperty i18nPartProperty = value == null ? null : value.get();
                // Although a i18nPartProperty is never null at initialization, it can be dropped by the GC since
                // it is contained in a WeakReference. If this happens, this means that the client software actually
                // doesn't use it (either never from the beginning or just not anymore after an activity is closed
                // for example), so we can just remove that entry to release some memory.
                if (i18nPartProperty == null) // Means the client software doesn't use this i18nKey,translationPart pair
                    it.remove(); // So we can drop this entry
                else // Otherwise, the client software still uses it so we need to update it
                    refreshI18nPart(i18nPartProperty, i18nKey, translationPartEntry.getKey());
            }
    }

    private StringProperty refreshI18nPart(StringProperty i18nPartProperty, Object i18nKey, I18nPart part) {
        if (dictionaryLoadRequired && dictionaryLoader != null)
            scheduleMessageLoading(i18nKey, false);
        else
            i18nPartProperty.setValue(getI18nPartValue(i18nKey, part));
        return i18nPartProperty;
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
                Set<Object> loadingKeys = getUnloadedKeys(inDefaultLanguage);
                dictionaryLoader.loadDictionary(language, loadingKeys).setHandler(asyncResult -> {
                    if (!inDefaultLanguage)
                        dictionaryProperty.setValue(asyncResult.result());
                    if (language.equals(getDefaultLanguage()))
                        defaultDictionaryProperty.setValue(asyncResult.result());
                    dictionaryLoadRequired = false;
                    for (Object key : loadingKeys)
                        refreshMessageTranslations(key);
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
