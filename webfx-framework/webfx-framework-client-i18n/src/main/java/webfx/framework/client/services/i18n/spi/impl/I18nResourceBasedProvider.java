package webfx.framework.client.services.i18n.spi.impl;

/**
 * @author Bruno Salmon
 */
public class I18nResourceBasedProvider extends I18nProviderImpl {

    public I18nResourceBasedProvider(String resourcePathWithLangPattern) {
        this(resourcePathWithLangPattern, null);
    }

    public I18nResourceBasedProvider(String resourcePathWithLangPattern, Object initialLanguage) {
        super(new ResourceJsonDictionaryLoader(resourcePathWithLangPattern), initialLanguage);
    }
}
