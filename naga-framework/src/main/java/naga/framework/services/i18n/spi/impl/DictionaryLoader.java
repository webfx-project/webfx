package naga.framework.services.i18n.spi.impl;

import naga.util.async.Future;
import naga.framework.services.i18n.Dictionary;

import java.util.Set;

/**
 * @author Bruno Salmon
 */
interface DictionaryLoader {

    Future<Dictionary> loadDictionary(Object lang, Set keys);

}
