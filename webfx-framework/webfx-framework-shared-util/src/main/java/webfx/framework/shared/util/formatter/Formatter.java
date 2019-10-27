package webfx.framework.shared.util.formatter;

import webfx.extras.type.Type;

/**
 * @author Bruno Salmon
 */
public interface Formatter {

    Type getOutputType();

    Object format(Object value);
}
