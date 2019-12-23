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
        SingleServiceProvider.registerServiceSupplier(I18nProvider.class, () -> ServiceLoader.load(I18nProvider.class));
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

    public static String getI18nText(Object i18nKey) {
        return getProvider().getI18nText(i18nKey);
    }

    public static ObservableStringValue i18nTextProperty(Object i18nKey) {
        return getProvider().i18nTextProperty(i18nKey);
    }

    public static I18nProvider bindI18nTextProperty(Property<String> textProperty, Object i18nKey) {
        return getProvider().bindI18nTextProperty(textProperty, i18nKey);
    }

    public static String getI18nGraphicUrl(Object i18nKey) {
        return getProvider().getI18nGraphicUrl(i18nKey);
    }

    public static ObservableStringValue i18nGraphicUrlProperty(Object i18nKey) {
        return getProvider().i18nGraphicUrlProperty(i18nKey);
    }

    public static String getI18nPrompt(Object i18nKey) {
        return getProvider().getI18nPrompt(i18nKey);
    }

    public static ObservableStringValue i18nPromptProperty(Object i18nKey) {
        return getProvider().i18nPromptProperty(i18nKey);
    }

    public static I18nProvider bindI18nPromptProperty(Property<String> promptProperty, Object i18nKey) {
        return getProvider().bindPromptProperty(promptProperty, i18nKey);
    }

    public static ObservableStringValue i18nPartProperty(Object i18nKey, I18nPart part) {
        return getProvider().i18nPartProperty(i18nKey, part);
    }

    public static void refreshMessageTranslations(Object i18nKey) {
        getProvider().refreshMessageTranslations(i18nKey);
    }

}
