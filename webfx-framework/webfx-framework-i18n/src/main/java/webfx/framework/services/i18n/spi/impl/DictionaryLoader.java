package webfx.framework.services.i18n.spi.impl;

import webfx.platforms.core.util.async.Future;
import webfx.framework.services.i18n.Dictionary;

import java.util.Set;

/**
 * @author Bruno Salmon
 */
interface DictionaryLoader {

    Future<Dictionary> loadDictionary(Object lang, Set keys);

}
