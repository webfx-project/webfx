package naga.toolkit.spi.nodes;

import javafx.collections.ObservableList;

/**
 * @author Bruno Salmon
 */
public interface Parent extends GuiNode {

    ObservableList<GuiNode> getChildren();

}
