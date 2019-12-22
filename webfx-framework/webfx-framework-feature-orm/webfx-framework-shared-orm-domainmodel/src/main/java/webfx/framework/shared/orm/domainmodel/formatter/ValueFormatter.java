package webfx.framework.shared.orm.domainmodel.formatter;

import webfx.extras.type.Type;

/**
 * @author Bruno Salmon
 */
public interface ValueFormatter {

    Type getFormattedValueType();

    Object formatValue(Object value);
}
