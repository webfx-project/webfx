package mongoose.client.activity.table;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.SplitPane;

import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

public class MasterSlaveView {

    private final SplitPane splitPane = new SplitPane();

    public MasterSlaveView() {
        this(Orientation.VERTICAL, null, null);
    }

    public MasterSlaveView(Orientation orientation, Node masterView, Node slaveView) {
        splitPane.setOrientation(orientation);
        setMasterView(masterView);
        setSlaveView(slaveView);
    }

    public SplitPane getSplitPane() {
        return splitPane;
    }

    private final ObjectProperty<Node> masterViewProperty = new SimpleObjectProperty<Node>() { // GWT doesn't accept <>
        @Override
        protected void invalidated() {
            updateView();
        }
    };

    public ObjectProperty<Node> masterViewProperty() {
        return masterViewProperty;
    }

    public void setMasterView(Node masterView) {
        masterViewProperty().set(masterView);
    }

    public Node getMasterView() {
        return masterViewProperty().get();
    }

    private final ObjectProperty<Node> slaveViewProperty = new SimpleObjectProperty<Node>() { // GWT doesn't accept <>
        @Override
        protected void invalidated() {
            updateView();
        }
    };

    public ObjectProperty<Node> slaveViewProperty() {
        return slaveViewProperty;
    }

    public void setSlaveView(Node slaveView) {
        slaveViewProperty().set(slaveView);
    }

    public Node getSlaveView() {
        return slaveViewProperty().get();
    }

    private final BooleanProperty slaveVisibleProperty = new SimpleBooleanProperty() {
        @Override
        protected void invalidated() {
            updateView();
        }
    };

    public BooleanProperty slaveVisibleProperty() {
        return slaveVisibleProperty;
    }

    public boolean isSlaveVisible() {
        return slaveVisibleProperty().get();
    }

    public void setSlaveVisible(boolean slaveVisible) {
        slaveVisibleProperty().setValue(slaveVisible);
    }

    void updateView() {
        updateSplitPane(getMasterView(), isSlaveVisible() ? getSlaveView() : null);
    }

    void updateSplitPane(Node... views) {
        splitPane.getItems().setAll(Arrays.stream(views).filter(Objects::nonNull).collect(Collectors.toList()));
    }
}
