package naga.core.spi.toolkit.nodes;

import javafx.collections.ObservableList;
import naga.core.spi.toolkit.GuiNode;

/**
 * @author Bruno Salmon
 */
public interface Parent<P, N> extends GuiNode<P> {

    ObservableList<GuiNode<N>> getChildren();

}
