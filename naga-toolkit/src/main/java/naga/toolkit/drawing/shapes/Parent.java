package naga.toolkit.drawing.shapes;

import javafx.collections.ObservableList;

/**
 * @author Bruno Salmon
 */
public interface Parent extends Node {

    ObservableList<Node> getChildren();
}
