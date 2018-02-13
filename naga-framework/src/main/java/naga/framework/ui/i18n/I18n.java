package naga.framework.ui.i18n;

import javafx.beans.property.Property;
import javafx.beans.value.ObservableStringValue;
import javafx.scene.control.Labeled;
import javafx.scene.control.TextInputControl;
import javafx.scene.text.Text;
import naga.framework.ui.i18n.impl.I18nImpl;
import naga.framework.ui.i18n.impl.ResourceDictionaryLoader;
import naga.util.Strings;

/**
 * @author Bruno Salmon
 */
public interface I18n {

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

    default I18n translateString(Property<String> stringProperty, Object key) {
        stringProperty.bind(translationProperty(key));
        return this;
    }

    default I18n translateTextFluent(Labeled labeled, Object key) {
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

    default I18n translatePromptTextFluent(TextInputControl textInputControl, Object key) {
        return translateString(textInputControl.promptTextProperty(), key);
    }

    default <T extends TextInputControl> T translatePromptText(T textInputControl, Object key) {
        translatePromptTextFluent(textInputControl, key);
        return textInputControl;
    }

    static I18n create(String langResourcePathPattern) {
        return new I18nImpl(new ResourceDictionaryLoader(langResourcePathPattern));
    }

}
