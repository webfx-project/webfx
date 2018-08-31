package webfx.framework.ui.formatter;

import webfx.type.Type;

/**
 * @author Bruno Salmon
 */
public interface Formatter {

    Type getOutputType();

    Object format(Object value);
}
