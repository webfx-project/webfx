package naga.framework.ui.i18n.impl;

import naga.commons.util.Strings;
import naga.commons.util.async.Future;
import naga.platform.spi.Platform;

import java.util.Set;

/**
 * @author Bruno Salmon
 */
public class ResourceDictionaryLoader implements DictionaryLoader {

    private final String pathPattern;

    public ResourceDictionaryLoader(String pathPattern) {
        this.pathPattern = pathPattern;
    }

    private String getDirectionayResourcePath(Object lang) {
        return Strings.replaceAll(pathPattern, "{lang}", Strings.toString(lang));
    }

    @Override
    public Future<Dictionary> loadDictionary(Object lang, Set keys) {
        Future<Dictionary> future = Future.future();
        Platform.getResourceService().getText(getDirectionayResourcePath(lang)).setHandler(asyncResult -> {
            if (asyncResult.failed())
                future.fail(asyncResult.cause());
            else
                future.complete(new JsonDictionary(asyncResult.result()));
        });
        return future;
    }
}
