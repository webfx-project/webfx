package naga.framework.ui.i18n.impl;

import naga.commons.util.async.Future;
import naga.framework.ui.i18n.Dictionary;

import java.util.Set;

/**
 * @author Bruno Salmon
 */
interface DictionaryLoader {

    Future<Dictionary> loadDictionary(Object lang, Set keys);

}
