package naga.framework.services.i18n.spi.impl;

import naga.framework.services.i18n.Dictionary;
import naga.platform.services.resource.ResourceService;
import naga.util.Strings;
import naga.util.async.Future;

import java.util.Set;

/**
 * @author Bruno Salmon
 */
public final class ResourceJsonDictionaryLoader implements DictionaryLoader {

    private final String jsonResourcePathWithLangPattern;

    public ResourceJsonDictionaryLoader(String jsonResourcePathWithLangPattern) {
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
