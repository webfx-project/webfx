package naga.core.spi.toolkit.node;

import javafx.collections.ObservableList;

/**
 * @author Bruno Salmon
 */
public interface Parent<P, N> extends GuiNode<P> {

    ObservableList<GuiNode<N>> getChildren();

}
