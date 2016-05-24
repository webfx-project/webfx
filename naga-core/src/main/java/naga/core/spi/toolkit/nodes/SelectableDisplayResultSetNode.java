package naga.core.spi.toolkit.nodes;

import naga.core.spi.toolkit.hasproperties.HasDisplaySelectionProperty;
import naga.core.spi.toolkit.hasproperties.HasSelectionModeProperty;

/**
 * @author Bruno Salmon
 */
public interface SelectableDisplayResultSetNode<N> extends DisplayResultSetNode<N>,
        HasDisplaySelectionProperty, HasSelectionModeProperty {
}
