package naga.framework.ui.i18n;

import javafx.beans.property.Property;
import javafx.scene.control.Labeled;
import javafx.scene.control.TextInputControl;
import javafx.scene.text.Text;
import naga.commons.util.Strings;
import naga.framework.ui.i18n.impl.I18nImpl;
import naga.framework.ui.i18n.impl.ResourceDictionaryLoader;

/**
 * @author Bruno Salmon
 */
public interface I18n {

    Property<Object> languageProperty();
    default Object getLanguage() { return languageProperty().getValue(); }
    default void setLanguage(Object language) { languageProperty().setValue(language); }

    Property<String> translationProperty(Object key);

    Property<Dictionary> dictionaryProperty();

    default Dictionary getDictionary() {
        return dictionaryProperty().getValue();
    }

    default String instantTranslate(Object key) {
        Dictionary dictionary = getDictionary();
        String message = dictionary == null ? null : dictionary.getMessage(key);
        if (message == null)
            message = notFoundTranslation(key);
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
