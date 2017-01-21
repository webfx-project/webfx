package naga.framework.ui.presentation;

import javafx.scene.Node;
import naga.framework.ui.presentation.ViewModel;

/**
 * @author Bruno Salmon
 */
public class ViewModelBase<N extends Node> implements ViewModel<N> {

    private final N contentNode;

    public ViewModelBase(N contentNode) {
        this.contentNode = contentNode;
    }

    @Override
    public N getContentNode() {
        return contentNode;
    }
}
