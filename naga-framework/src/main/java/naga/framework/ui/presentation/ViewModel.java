package naga.framework.ui.presentation;

import naga.toolkit.spi.nodes.GuiNode;

/**
 * @author Bruno Salmon
 */
public interface ViewModel<N extends GuiNode> {

    N getContentNode();

}
