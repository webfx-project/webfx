package webfx.framework.services.i18n.spi;

import javafx.beans.property.Property;
import javafx.beans.value.ObservableStringValue;
import javafx.scene.control.Labeled;
import javafx.scene.control.TextInputControl;
import javafx.scene.text.Text;
import webfx.framework.services.i18n.Dictionary;
import webfx.framework.services.i18n.spi.impl.I18nProviderImpl;
import webfx.framework.services.i18n.spi.impl.ResourceJsonDictionaryLoader;
import webfx.util.Strings;

/**
 * @author Bruno Salmon
 */
public interface I18nProvider {

    Property<Object> languageProperty();
    default Object getLanguage() { return languageProperty().getValue(); }
    default void setLanguage(Object language) { languageProperty().setValue(language); }

    ObservableStringValue translationProperty(Object key);

    Property<Dictionary> dictionaryProperty();

    default Dictionary getDictionary() {
        return dictionaryProperty().getValue();
    }

    default String instantTranslate(Object key) {
        Dictionary dictionary = getDictionary();
        String message = null;
        if (dictionary != null && key != null) {
            message = dictionary.getMessage(key);
            if (message == null) {
                String sKey = Strings.asString(key);
                int length = Strings.length(sKey);
                if (length > 1) {
                    int index = 0;
                    while (index < length && !Character.isLetterOrDigit(sKey.charAt(index)))
                        index++;
                    if (index > 0) {
                        String prefix = sKey.substring(0, index);
                        switch (prefix) {
                            case "<<":
                                message = instantTranslate(prefix) + instantTranslate(sKey.substring(prefix.length(), length));
                        }
                    }
                }
                if (message == null && length > 1) {
                    int index = length;
                    while (index > 0 && !Character.isLetterOrDigit(sKey.charAt(index - 1)))
                        index--;
                    if (index < length) {
                        String suffix = sKey.substring(index, length);
                        switch (suffix) {
                            case ":":
                            case "?":
                            case ">>":
                                message = instantTranslate(sKey.substring(0, length - suffix.length())) + instantTranslate(suffix);
                        }
                    }
                }
                if (message == null)
                    message = notFoundTranslation(key);
            }
        }
        return message;
    }

    default <T extends Labeled> T instantTranslateText(T labeled, Object key) {
        labeled.setText(instantTranslate(key));
        return labeled;
    }

    default String notFoundTranslation(Object key) {
        return Strings.toString(key);
    }

    default I18nProvider translateString(Property<String> stringProperty, Object key) {
        stringProperty.bind(translationProperty(key));
        return this;
    }

    default I18nProvider translateTextFluent(Labeled labeled, Object key) {
        return translateString(labeled.textProperty(), key);
    }

    default <T extends Text> T translateText(T text, Object key) {
        translateString(text.textProperty(), key);
        return text;
    }

    default <T extends Labeled> T translateText(T labeled, Object key) {
        translateTextFluent(labeled, key);
        return labeled;
    }

    default I18nProvider translatePromptTextFluent(TextInputControl textInputControl, Object key) {
        return translateString(textInputControl.promptTextProperty(), key);
    }

    default <T extends TextInputControl> T translatePromptText(T textInputControl, Object key) {
        translatePromptTextFluent(textInputControl, key);
        return textInputControl;
    }

    static I18nProvider createFromJsonResources(String resourcePathWithLangPattern) {
        return new I18nProviderImpl(new ResourceJsonDictionaryLoader(resourcePathWithLangPattern));
    }

}
