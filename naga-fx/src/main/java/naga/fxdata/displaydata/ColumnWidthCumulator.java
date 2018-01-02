package naga.fxdata.displaydata;

import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.Node;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Bruno Salmon
 */
public class ColumnWidthCumulator {

    private double maxWidth;
    private boolean hasChanged;
    private List<ObservableList<Node>> severalColumnNodes;

    public void reset() {
        maxWidth = 0;
    }

    public void registerColumnNodes(ObservableList<Node> columnNodes) {
        if (severalColumnNodes == null)
            severalColumnNodes = new ArrayList<>();
        severalColumnNodes.add(columnNodes);
        columnNodes.addListener((ListChangeListener<Node>) c -> hasChanged = true);
        if (!columnNodes.isEmpty())
            hasChanged = true;
    }

    public void update() {
        if (hasChanged) {
            for (ObservableList<Node> columnNodes : severalColumnNodes)
                for (Node node : columnNodes)
                    cumulate(node);
            hasChanged = false;
        }
    }

    public void cumulate(Node cellContent) {
        cumulate(cellContent.minWidth(-1));
    }

    public void cumulate(double columnWidth) {
        if (columnWidth > maxWidth)
            maxWidth = columnWidth;
    }

    public double getMaxWidth() {
        return maxWidth;
    }
}