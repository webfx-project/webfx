package mongoose.backend.controls.masterslave;

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

public class MasterSlaveView implements UiBuilder {

    private final SplitPane splitPane = new SplitPane();

    public MasterSlaveView() {
        this(null, null);
    }

    public MasterSlaveView(Node masterView, Node slaveView) {
        setMasterView(masterView);
        setSlaveView(slaveView);
        setOrientation(Orientation.VERTICAL); // Vertical orientation by default
    }

    public MasterSlaveView setOrientation(Orientation orientation) {
        splitPane.setOrientation(orientation);
        return this;
    }

    @Override
    public Node buildUi() {
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

    protected void updateView() {
        updateSplitPane(getMasterView(), isSlaveVisible() ? getSlaveView() : null);
    }

    protected void updateSplitPane(Node... views) {
        splitPane.getItems().setAll(Arrays.stream(views).filter(Objects::nonNull).collect(Collectors.toList()));
    }
}
