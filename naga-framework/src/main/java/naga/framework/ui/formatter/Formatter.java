package naga.framework.ui.formatter;

import naga.type.Type;

/**
 * @author Bruno Salmon
 */
public interface Formatter {

    Type getOutputType();

    Object format(Object value);
}
