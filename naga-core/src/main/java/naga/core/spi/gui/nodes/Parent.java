package naga.core.spi.gui.nodes;

import javafx.collections.ObservableList;
import naga.core.spi.gui.GuiNode;

/**
 * @author Bruno Salmon
 */
public interface Parent<P, N> extends GuiNode<P> {

    ObservableList<GuiNode<N>> getChildren();

}
