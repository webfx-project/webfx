package naga.framework.ui.presentation;

import naga.toolkit.spi.nodes.GuiNode;

/**
 * @author Bruno Salmon
 */
public abstract class AbstractViewModel<N extends GuiNode> implements ViewModel<N> {

    private final N contentNode;

    protected AbstractViewModel(N contentNode) {
        this.contentNode = contentNode;
    }

    @Override
    public N getContentNode() {
        return contentNode;
    }
}
