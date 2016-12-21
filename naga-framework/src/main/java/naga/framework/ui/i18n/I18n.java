package naga.framework.ui.i18n;

import javafx.beans.property.Property;
import naga.commons.util.Strings;
import naga.framework.ui.i18n.impl.I18nImpl;
import naga.framework.ui.i18n.impl.ResourceDictionaryLoader;
import naga.toolkit.fx.properties.markers.HasPromptTextProperty;
import naga.toolkit.fx.properties.markers.HasTextProperty;

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

    default <T extends HasTextProperty> T instantTranslateText(T hasTextProperty, Object key) {
        hasTextProperty.setText(instantTranslate(key));
        return hasTextProperty;
    }

    default String notFoundTranslation(Object key) {
        return Strings.toString(key);
    }

    default I18n translateString(Property<String> stringProperty, Object key) {
        stringProperty.bind(translationProperty(key));
        return this;
    }

    default I18n translateTextFluent(HasTextProperty hasTextProperty, Object key) {
        return translateString(hasTextProperty.textProperty(), key);
    }

    default <T extends HasTextProperty> T translateText(T hasTextProperty, Object key) {
        translateTextFluent(hasTextProperty, key);
        return hasTextProperty;
    }

    default I18n translatePromptTextFluent(HasPromptTextProperty hasPromptTextProperty, Object key) {
        return translateString(hasPromptTextProperty.promptTextProperty(), key);
    }

    default <T extends HasPromptTextProperty> T translatePromptText(T hasPromptTextProperty, Object key) {
        translatePromptTextFluent(hasPromptTextProperty, key);
        return hasPromptTextProperty;
    }

    static I18n create(String langResourcePathPattern) {
        return new I18nImpl(new ResourceDictionaryLoader(langResourcePathPattern));
    }

}
