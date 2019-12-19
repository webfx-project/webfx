package webfx.framework.client.orm.reactive.mapping.entities_to_grid.formatter;

import webfx.extras.type.Type;

/**
 * @author Bruno Salmon
 */
public interface ValueFormatter {

    Type getFormattedValueType();

    Object formatValue(Object value);
}
