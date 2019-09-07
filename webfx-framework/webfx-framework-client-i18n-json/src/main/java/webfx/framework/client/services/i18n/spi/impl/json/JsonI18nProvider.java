package webfx.framework.client.services.i18n.spi.impl.json;


import webfx.framework.client.services.i18n.spi.impl.I18nProviderImpl;

/**
 * @author Bruno Salmon
 */
public class JsonI18nProvider extends I18nProviderImpl {

    public JsonI18nProvider(String resourcePathWithLangPattern) {
        this(resourcePathWithLangPattern, null);
    }

    public JsonI18nProvider(String resourcePathWithLangPattern, Object defaultLanguage) {
        this(resourcePathWithLangPattern, defaultLanguage, null);
    }

    public JsonI18nProvider(String resourcePathWithLangPattern, Object defaultLanguage, Object initialLanguage) {
        super(new ResourceJsonDictionaryLoader(resourcePathWithLangPattern), defaultLanguage, initialLanguage);
    }
}
