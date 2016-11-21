package naga.toolkit.drawing.shapes.impl;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import naga.toolkit.drawing.shapes.Node;
import naga.toolkit.drawing.shapes.Parent;
import naga.toolkit.util.ObservableLists;

/**
 * @author Bruno Salmon
 */
public class ParentImpl extends NodeImpl implements Parent {

    public ParentImpl() {
    }

    public ParentImpl(Node... nodes) {
        ObservableLists.setAllNonNulls(getChildren(), nodes);
    }

    private final ObservableList<Node> children = FXCollections.observableArrayList();
    public ObservableList<Node> getChildren() {
        return children;
    }

}
