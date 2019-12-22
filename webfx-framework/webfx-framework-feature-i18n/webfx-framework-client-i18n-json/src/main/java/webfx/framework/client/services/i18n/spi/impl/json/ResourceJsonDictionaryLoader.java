package webfx.framework.client.services.i18n.spi.impl.json;

import webfx.framework.client.services.i18n.Dictionary;
import webfx.framework.client.services.i18n.spi.impl.DictionaryLoader;
import webfx.platform.shared.services.resource.ResourceService;
import webfx.platform.shared.util.Strings;
import webfx.platform.shared.util.async.Future;

import java.util.Set;

/**
 * @author Bruno Salmon
 */
final class ResourceJsonDictionaryLoader implements DictionaryLoader {

    private final String jsonResourcePathWithLangPattern;

    ResourceJsonDictionaryLoader(String jsonResourcePathWithLangPattern) {
        this.jsonResourcePathWithLangPattern = jsonResourcePathWithLangPattern;
    }

    private String getDictionaryResourcePath(Object lang) {
        return Strings.replaceAll(jsonResourcePathWithLangPattern, "{lang}", Strings.toString(lang));
    }

    @Override
    public Future<Dictionary> loadDictionary(Object lang, Set keys) {
        return ResourceService.getText(getDictionaryResourcePath(lang)).map(JsonDictionary::new);
    }
}
