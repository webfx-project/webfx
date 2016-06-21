package naga.core.format;

import naga.core.type.Type;

/**
 * @author Bruno Salmon
 */
public interface Formatter {

    Type getExpectedFormattedType();

    Object format(Object value);
}
