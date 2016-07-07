package naga.toolkit.spi.nodes.controls;

import naga.toolkit.spi.nodes.GuiNode;
import naga.toolkit.spi.properties.markers.HasPlaceholderProperty;
import naga.toolkit.spi.properties.markers.HasTextProperty;

/**
 * @author Bruno Salmon
 */
public interface TextField<N> extends GuiNode<N>, HasTextProperty, HasPlaceholderProperty {

}