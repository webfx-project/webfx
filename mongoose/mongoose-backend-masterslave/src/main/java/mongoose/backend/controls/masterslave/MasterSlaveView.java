package mongoose.backend.controls.masterslave;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.Pane;
import mongoose.backend.controls.masterslave.group.GroupView;
import webfx.fxkit.util.properties.Properties;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
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
        splitPane.getProperties().put("masterSlaveView", this); // just to prevent GC
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


    /*==================================================================================================================
    =================================================== Data binding ===================================================
    ==================================================================================================================*/

    public <E> void doVisibilityBinding(GroupView groupView, ObjectProperty<E> masterSelectedEntityProperty, Function<E, Boolean> additionalSlaveVisibilityCondition) {
        slaveVisibleProperty().bind(Properties.compute(masterSelectedEntityProperty, selectedEntity -> selectedEntity != null && (additionalSlaveVisibilityCondition == null || additionalSlaveVisibilityCondition.apply(selectedEntity))));
    }


    /*==================================================================================================================
    =========================================== Slave view builder registry ============================================
    ==================================================================================================================*/

    @FunctionalInterface
    public interface SlaveViewBuilder {
        UiBuilder createAndBindSlaveViewIfApplicable(Object pm, Object mixin, Pane container);
    }

    private final static List<SlaveViewBuilder> slaveViewBuilders = new ArrayList<>();

    public static void registerSlaveViewBuilder(SlaveViewBuilder slaveViewBuilder) {
        slaveViewBuilders.add(slaveViewBuilder);
    }

    public static UiBuilder createAndBindSlaveViewIfApplicable(Object pm, Object mixin, Pane container) {
        return slaveViewBuilders.stream().map(svb -> svb.createAndBindSlaveViewIfApplicable(pm, mixin, container)).filter(Objects::nonNull).findFirst().orElse(null);
    }
}
