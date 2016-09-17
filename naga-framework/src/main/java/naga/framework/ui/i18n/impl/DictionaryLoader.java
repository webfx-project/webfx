package naga.framework.ui.i18n.impl;

import naga.commons.util.async.Future;

import java.util.Set;

/**
 * @author Bruno Salmon
 */
public interface DictionaryLoader {

    Future<Dictionary> loadDictionary(Object lang, Set keys);

}
