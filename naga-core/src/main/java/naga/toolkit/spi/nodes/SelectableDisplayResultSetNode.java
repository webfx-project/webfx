package naga.toolkit.spi.nodes;

import naga.toolkit.spi.properties.markers.HasDisplaySelectionProperty;
import naga.toolkit.spi.properties.markers.HasSelectionModeProperty;

/**
 * @author Bruno Salmon
 */
public interface SelectableDisplayResultSetNode<N> extends DisplayResultSetNode<N>,
        HasDisplaySelectionProperty, HasSelectionModeProperty {
}
