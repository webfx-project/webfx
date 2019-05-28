package webfx.framework.client.services.i18n.spi.impl;

import webfx.platform.shared.util.async.Future;
import webfx.framework.client.services.i18n.Dictionary;

import java.util.Set;

/**
 * @author Bruno Salmon
 */
public interface DictionaryLoader {

    Future<Dictionary> loadDictionary(Object lang, Set keys);

}
