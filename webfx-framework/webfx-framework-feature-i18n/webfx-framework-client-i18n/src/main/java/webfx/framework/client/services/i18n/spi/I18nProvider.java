package webfx.framework.client.services.i18n.spi;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.Property;
import javafx.beans.value.ObservableObjectValue;
import javafx.beans.value.ObservableStringValue;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import webfx.framework.client.operations.i18n.ChangeLanguageRequestEmitter;
import webfx.framework.client.services.i18n.Dictionary;
import webfx.framework.client.services.i18n.I18nPart;
import webfx.platform.shared.util.collection.Collections;

import java.util.Collection;
import java.util.ServiceLoader;

/**
 * @author Bruno Salmon
 */
public interface I18nProvider {

    default Collection<Object> getSupportedLanguages() {
        return Collections.map(getProvidedInstantiators(), i -> i.emitLanguageRequest().getLanguage());
    }

    default Collection<ChangeLanguageRequestEmitter> getProvidedInstantiators() {
        return Collections.listOf(ServiceLoader.load(ChangeLanguageRequestEmitter.class));
    }

    ObjectProperty<Object> languageProperty();
    default Object getLanguage() { return languageProperty().getValue(); }
    default void setLanguage(Object language) { languageProperty().setValue(language); }

    ObservableObjectValue<Dictionary> dictionaryProperty();
    default Dictionary getDictionary() {
        return dictionaryProperty().getValue();
    }

    Object getDefaultLanguage();
    Dictionary getDefaultDictionary();

    default String getI18nText(Object i18nKey) {
        return getI18nPartValue(i18nKey, I18nPart.TEXT);
    }

    default ObservableStringValue i18nTextProperty(Object i18nKey) {
        return i18nPartProperty(i18nKey, I18nPart.TEXT);
    }

    default String getI18nGraphicUrl(Object i18nKey) {
        return getI18nPartValue(i18nKey, I18nPart.GRAPHIC);
    }

    default ObservableStringValue i18nGraphicUrlProperty(Object i18nKey) {
        return i18nPartProperty(i18nKey, I18nPart.GRAPHIC);
    }

    default String getI18nPrompt(Object i18nKey) {
        return getI18nPartValue(i18nKey, I18nPart.PROMPT);
    }

    default ObservableStringValue i18nPromptProperty(Object i18nKey) {
        return i18nPartProperty(i18nKey, I18nPart.PROMPT);
    }

    ObservableStringValue i18nPartProperty(Object i18nKey, I18nPart part);

    String getI18nPartValue(Object i18nKey, I18nPart part);

    void scheduleMessageLoading(Object i18nKey, boolean inDefaultLanguage);

    void refreshMessageTranslations(Object i18nKey);

    default I18nProvider bindI18nTextProperty(Property<String> textProperty, Object i18nKey) {
        textProperty.bind(i18nTextProperty(i18nKey));
        return this;
    }

    default I18nProvider bindPromptProperty(Property<String> promptProperty, Object i18nKey) {
        promptProperty.bind(i18nPromptProperty(i18nKey));
        return this;
    }

    default Node createI18nGraphic(String graphicUrl) {
        return graphicUrl == null || "".equals(graphicUrl) ? null : new ImageView(graphicUrl);
    }

}
