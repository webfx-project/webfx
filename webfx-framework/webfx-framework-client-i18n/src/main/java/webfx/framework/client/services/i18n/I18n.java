package webfx.framework.client.services.i18n;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.Property;
import javafx.beans.value.ObservableObjectValue;
import javafx.beans.value.ObservableStringValue;
import webfx.framework.client.operations.i18n.ChangeLanguageRequestEmitter;
import webfx.framework.client.services.i18n.spi.I18nProvider;
import webfx.platform.shared.util.serviceloader.SingleServiceProvider;

import java.util.Collection;
import java.util.ServiceLoader;

/**
 * @author Bruno Salmon
 */
public final class I18n {

    static {
        SingleServiceProvider.register(I18nProvider.class, () -> ServiceLoader.load(I18nProvider.class));
    }

    public static I18nProvider getProvider() {
        return SingleServiceProvider.getProvider(I18nProvider.class);
    }

    public static Collection<ChangeLanguageRequestEmitter> getProvidedInstantiators() {
        return getProvider().getProvidedInstantiators();
    }

    public static Collection<Object> getSupportedLanguages() {
        return getProvider().getSupportedLanguages();
    }

    public static ObjectProperty<Object> languageProperty() {
        return getProvider().languageProperty();
    }

    public static Object getLanguage() {
        return getProvider().getLanguage();
    }

    public static void setLanguage(Object language) {
        getProvider().setLanguage(language);
    }

    public static ObservableObjectValue<Dictionary> dictionaryProperty() {
        return getProvider().dictionaryProperty();
    }

    public static Dictionary getDictionary() {
        return getProvider().getDictionary();
    }

    public static String instantTranslateText(Object i18nKey) {
        return getProvider().instantTranslateText(i18nKey);
    }

    public static ObservableStringValue observableText(Object i18nKey) {
        return getProvider().observableText(i18nKey);
    }

    public static I18nProvider translateTextProperty(Property<String> textProperty, Object i18nKey) {
        return getProvider().translateTextProperty(textProperty, i18nKey);
    }

    public static String instantTranslateGraphic(Object i18nKey) {
        return getProvider().instantTranslateGraphic(i18nKey);
    }

    public static ObservableStringValue observableGraphic(Object i18nKey) {
        return getProvider().observableGraphic(i18nKey);
    }

    public static ObservableStringValue observablePart(Object i18nKey, TranslationPart part) {
        return getProvider().observablePart(i18nKey, part);
    }


}
