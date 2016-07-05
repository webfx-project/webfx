package naga.core.spi.toolkit.controls;

import naga.core.spi.toolkit.node.GuiNode;
import naga.core.spi.toolkit.propertymarkers.HasPlaceholderProperty;
import naga.core.spi.toolkit.propertymarkers.HasTextProperty;

/**
 * @author Bruno Salmon
 */
public interface TextField<N> extends GuiNode<N>, HasTextProperty, HasPlaceholderProperty {

}