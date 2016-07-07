package naga.framework.ui.format;

import naga.commons.type.Type;

/**
 * @author Bruno Salmon
 */
public interface Formatter {

    Type getExpectedFormattedType();

    Object format(Object value);
}
