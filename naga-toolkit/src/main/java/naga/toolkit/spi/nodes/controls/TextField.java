package naga.toolkit.spi.nodes.controls;

import naga.toolkit.spi.nodes.GuiNode;
import naga.toolkit.properties.markers.HasTextProperty;
import naga.toolkit.properties.markers.HasPlaceholderProperty;

/**
 * @author Bruno Salmon
 */
public interface TextField<N> extends GuiNode<N>, HasTextProperty, HasPlaceholderProperty {

}