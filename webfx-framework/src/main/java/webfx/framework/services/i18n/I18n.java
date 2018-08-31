package webfx.framework.services.i18n;

import javafx.beans.property.Property;
import javafx.beans.value.ObservableStringValue;
import javafx.scene.control.Labeled;
import javafx.scene.control.TextInputControl;
import javafx.scene.text.Text;
import webfx.framework.services.i18n.spi.I18nProvider;
import webfx.util.serviceloader.ServiceLoaderHelper;

/**
 * @author Bruno Salmon
 */
public final class I18n {

    public static I18nProvider getProvider() {
        return ServiceLoaderHelper.loadService(I18nProvider.class);
    }

    public static void registerProvider(I18nProvider provider) {
        ServiceLoaderHelper.cacheServiceInstance(I18nProvider.class, provider);
    }

    public static Property<Object> languageProperty() {
        return getProvider().languageProperty();
    }

    public static Object getLanguage() { return getProvider().getLanguage(); }
    public static void setLanguage(Object language) { getProvider().setLanguage(language); }

    public static ObservableStringValue translationProperty(Object key) {
        return getProvider().translationProperty(key);
    }

    public static Property<Dictionary> dictionaryProperty() {
        return getProvider().dictionaryProperty();
    }

    public static Dictionary getDictionary() {
        return getProvider().getDictionary();
    }

    public static String instantTranslate(Object key) {
        return getProvider().instantTranslate(key);
    }

    public static <T extends Labeled> T instantTranslateText(T labeled, Object key) {
        return getProvider().instantTranslateText(labeled, key);
    }

    public static String notFoundTranslation(Object key) {
        return getProvider().notFoundTranslation(key);
    }

    public static I18nProvider translateString(Property<String> stringProperty, Object key) {
        return getProvider().translateString(stringProperty, key);
    }

    public static I18nProvider translateTextFluent(Labeled labeled, Object key) {
        return translateString(labeled.textProperty(), key);
    }

    public static <T extends Text> T translateText(T text, Object key) {
        return getProvider().translateText(text, key);
    }

    public static <T extends Labeled> T translateText(T labeled, Object key) {
        return getProvider().translateText(labeled, key);
    }

    public static I18nProvider translatePromptTextFluent(TextInputControl textInputControl, Object key) {
        return getProvider().translatePromptTextFluent(textInputControl, key);
    }

    public static <T extends TextInputControl> T translatePromptText(T textInputControl, Object key) {
        return getProvider().translatePromptText(textInputControl, key);
    }
    
}
