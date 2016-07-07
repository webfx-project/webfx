package naga.core.spi.toolkit.node;

import naga.core.spi.toolkit.propertymarkers.HasDisplaySelectionProperty;
import naga.core.spi.toolkit.propertymarkers.HasSelectionModeProperty;

/**
 * @author Bruno Salmon
 */
public interface SelectableDisplayResultSetNode<N> extends DisplayResultSetNode<N>,
        HasDisplaySelectionProperty, HasSelectionModeProperty {
}
