package naga.framework.ui.i18n.impl;

import naga.util.async.Future;
import naga.framework.ui.i18n.Dictionary;

import java.util.Set;

/**
 * @author Bruno Salmon
 */
interface DictionaryLoader {

    Future<Dictionary> loadDictionary(Object lang, Set keys);

}
