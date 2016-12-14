package naga.framework.ui.presentation;

import naga.toolkit.fx.scene.Node;

/**
 * @author Bruno Salmon
 */
public class AbstractViewModel<N extends Node> implements ViewModel<N> {

    private final N contentNode;

    public AbstractViewModel(N contentNode) {
        this.contentNode = contentNode;
    }

    @Override
    public N getContentNode() {
        return contentNode;
    }
}
