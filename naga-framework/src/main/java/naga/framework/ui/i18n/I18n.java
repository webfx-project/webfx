package naga.framework.ui.i18n;

import javafx.beans.property.Property;
import naga.framework.ui.i18n.impl.I18nImpl;
import naga.framework.ui.i18n.impl.ResourceDictionaryLoader;
import naga.toolkit.properties.markers.HasPlaceholderProperty;
import naga.toolkit.properties.markers.HasTextProperty;

/**
 * @author Bruno Salmon
 */
public interface I18n {

    Property<Object> languageProperty();
    default Object getLanguage() { return languageProperty().getValue(); }
    default void setLanguage(Object language) { languageProperty().setValue(language); }

    Property<String> translationProperty(Object key);

    default String instantTranslate(Object key) {
        return translationProperty(key).getValue();
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

    default I18n translatePlaceholderFluent(HasPlaceholderProperty hasPlaceholderProperty, Object key) {
        return translateString(hasPlaceholderProperty.placeholderProperty(), key);
    }

    default <T extends HasPlaceholderProperty> T translatePlaceholder(T hasPlaceholderProperty, Object key) {
        translatePlaceholderFluent(hasPlaceholderProperty, key);
        return hasPlaceholderProperty;
    }

    static I18n create(String langResourcePathPattern) {
        return new I18nImpl(new ResourceDictionaryLoader(langResourcePathPattern));
    }

}
