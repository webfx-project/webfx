package naga.framework.services.i18n.spi.impl;

import naga.framework.services.i18n.Dictionary;
import naga.platform.services.resource.ResourceService;
import naga.util.Strings;
import naga.util.async.Future;

import java.util.Set;

/**
 * @author Bruno Salmon
 */
public class ResourceDictionaryLoader implements DictionaryLoader {

    private final String pathPattern;

    public ResourceDictionaryLoader(String pathPattern) {
        this.pathPattern = pathPattern;
    }

    private String getDictionaryResourcePath(Object lang) {
        return Strings.replaceAll(pathPattern, "{lang}", Strings.toString(lang));
    }

    @Override
    public Future<Dictionary> loadDictionary(Object lang, Set keys) {
        return ResourceService.getText(getDictionaryResourcePath(lang)).map(JsonDictionary::new);
    }
}
