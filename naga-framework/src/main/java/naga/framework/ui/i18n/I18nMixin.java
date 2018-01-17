package naga.framework.ui.i18n;

import javafx.beans.property.Property;
import javafx.scene.control.Labeled;
import javafx.scene.control.TextInputControl;
import javafx.scene.text.Text;

/**
 * @author Bruno Salmon
 */
public interface I18nMixin extends HasI18n, I18n {

    default Property<Object> languageProperty() {
        return getI18n().languageProperty();
    }

    default Object getLanguage() { return getI18n().getLanguage(); }
    default void setLanguage(Object language) { getI18n().setLanguage(language); }

    default Property<String> translationProperty(Object key) {
        return getI18n().translationProperty(key);
    }

    default Property<Dictionary> dictionaryProperty() {
        return getI18n().dictionaryProperty();
    }

    default Dictionary getDictionary() {
        return getI18n().getDictionary();
    }

    default String instantTranslate(Object key) {
        return getI18n().instantTranslate(key);
    }

    default <T extends Labeled> T instantTranslateText(T labeled, Object key) {
        return getI18n().instantTranslateText(labeled, key);
    }

    default String notFoundTranslation(Object key) {
        return getI18n().notFoundTranslation(key);
    }

    default I18n translateString(Property<String> stringProperty, Object key) {
        return getI18n().translateString(stringProperty, key);
    }

    default I18n translateTextFluent(Labeled labeled, Object key) {
        return translateString(labeled.textProperty(), key);
    }

    default <T extends Text> T translateText(T text, Object key) {
        return getI18n().translateText(text, key);
    }

    default <T extends Labeled> T translateText(T labeled, Object key) {
        return getI18n().translateText(labeled, key);
    }

    default I18n translatePromptTextFluent(TextInputControl textInputControl, Object key) {
        return getI18n().translatePromptTextFluent(textInputControl, key);
    }

    default <T extends TextInputControl> T translatePromptText(T textInputControl, Object key) {
        return getI18n().translatePromptText(textInputControl, key);
    }

}
