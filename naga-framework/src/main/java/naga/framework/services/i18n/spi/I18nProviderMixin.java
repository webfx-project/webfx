package naga.framework.services.i18n.spi;

import javafx.beans.property.Property;
import javafx.beans.value.ObservableStringValue;
import javafx.scene.control.Labeled;
import javafx.scene.control.TextInputControl;
import javafx.scene.text.Text;
import naga.framework.services.i18n.Dictionary;

/**
 * @author Bruno Salmon
 */
public interface I18nProviderMixin extends HasI18nProvider, I18nProvider {

    default Property<Object> languageProperty() {
        return getI18n().languageProperty();
    }

    default Object getLanguage() { return getI18n().getLanguage(); }
    default void setLanguage(Object language) { getI18n().setLanguage(language); }

    default ObservableStringValue translationProperty(Object key) {
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

    default I18nProvider translateString(Property<String> stringProperty, Object key) {
        return getI18n().translateString(stringProperty, key);
    }

    default I18nProvider translateTextFluent(Labeled labeled, Object key) {
        return translateString(labeled.textProperty(), key);
    }

    default <T extends Text> T translateText(T text, Object key) {
        return getI18n().translateText(text, key);
    }

    default <T extends Labeled> T translateText(T labeled, Object key) {
        return getI18n().translateText(labeled, key);
    }

    default I18nProvider translatePromptTextFluent(TextInputControl textInputControl, Object key) {
        return getI18n().translatePromptTextFluent(textInputControl, key);
    }

    default <T extends TextInputControl> T translatePromptText(T textInputControl, Object key) {
        return getI18n().translatePromptText(textInputControl, key);
    }

}
