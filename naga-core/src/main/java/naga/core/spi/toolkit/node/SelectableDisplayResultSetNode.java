package naga.core.spi.toolkit.node;

import naga.core.spi.toolkit.hasproperties.HasDisplaySelectionProperty;
import naga.core.spi.toolkit.hasproperties.HasSelectionModeProperty;

/**
 * @author Bruno Salmon
 */
public interface SelectableDisplayResultSetNode<N> extends DisplayResultSetNode<N>,
        HasDisplaySelectionProperty, HasSelectionModeProperty {
}
