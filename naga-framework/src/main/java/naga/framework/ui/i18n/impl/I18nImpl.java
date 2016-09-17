package naga.framework.ui.i18n.impl;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import naga.commons.util.Strings;
import naga.framework.ui.i18n.I18n;
import naga.toolkit.spi.Toolkit;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.*;

/**
 * @author Bruno Salmon
 */
public class I18nImpl implements I18n {

    private Map<Object, Reference<Property<String>>> translations = new HashMap<>();
    private DictionaryLoader dictionaryLoader;
    private Dictionary dictionary;
    private Set<Object> unloadedKeys;

    public I18nImpl(DictionaryLoader dictionaryLoader) {
        this.dictionaryLoader = dictionaryLoader;
        languageProperty.addListener((observable, oldValue, newValue) -> onLanguageChanged());
    }

    private Property<Object> languageProperty = new SimpleObjectProperty<>("fr");
    @Override
    public Property<Object> languageProperty() {
        return languageProperty;
    }

    @Override
    public Property<String> translationProperty(Object key) {
        Property<String> translationProperty = getTranslationProperty(key);
        if (translationProperty == null)
            translations.put(key, new WeakReference<>(translationProperty = createTranslationProperty(key)));
        return translationProperty;
    }

    private Property<String> getTranslationProperty(Object key) {
        Reference<Property<String>> ref = translations.get(key);
        return ref == null ? null : ref.get();
    }

    private Property<String> createTranslationProperty(Object key) {
        return updateTranslation(new SimpleObjectProperty<>(), key);
    }

    private void onLanguageChanged() {
        dictionary = null;
        updateTranslations();
    }

    private void updateTranslations() {
        for (Iterator<Map.Entry<Object, Reference<Property<String>>>> it = translations.entrySet().iterator(); it.hasNext(); ) {
            Map.Entry<Object, Reference<Property<String>>> entry = it.next();
            Reference<Property<String>> value = entry.getValue();
            Property<String> translationProperty = value == null ? null : value.get();
            if (translationProperty == null)
                it.remove();
            else
                updateTranslation(translationProperty, entry.getKey());
        }
    }

    private Property<String> updateTranslation(Property<String> translationProperty, Object key) {
        if (dictionary == null && dictionaryLoader != null) {
            if (unloadedKeys != null)
                unloadedKeys.add(key);
            else {
                unloadedKeys = new HashSet<>();
                unloadedKeys.add(key);
                Toolkit.get().scheduler().scheduleDeferred(() -> {
                    dictionaryLoader.loadDictionary(getLanguage(), unloadedKeys).setHandler(asyncResult -> {
                        dictionary = asyncResult.result();
                        updateTranslations();
                    });
                    unloadedKeys = null;
                });
            }
        } else
            translationProperty.setValue(getDictionaryMessage(key));
        return translationProperty;
    }

    private String getDictionaryMessage(Object key) {
        String message = dictionary == null ? null : dictionary.getMessage(key);
        if (message == null)
            message = notFoundTranslation(key);
        return message;
    }

    private String notFoundTranslation(Object key) {
        return Strings.toString(key);
    }
}
