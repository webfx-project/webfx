package naga.framework.ui.formatter;

import naga.type.Type;

/**
 * @author Bruno Salmon
 */
public interface Formatter {

    Type getExpectedFormattedType();

    Object format(Object value);
}
