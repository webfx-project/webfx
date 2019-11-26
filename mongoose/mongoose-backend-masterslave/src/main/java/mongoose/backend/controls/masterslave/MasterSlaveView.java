package mongoose.backend.controls.masterslave;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import mongoose.backend.controls.masterslave.group.GroupView;
import webfx.framework.shared.orm.entity.Entity;
import webfx.kit.util.properties.Properties;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class MasterSlaveView implements UiBuilder {

    private final SplitPane splitPane = new SplitPane();
    private final StackPane stackPane = new StackPane(splitPane); // Embedding the split pane in a stack pane to be able to act as a parent pane for the dialog API

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
    public Pane buildUi() {
        return stackPane;
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

    public <E extends Entity> void doVisibilityBinding(GroupView<E> groupView, ObjectProperty<E> masterSelectedEntityProperty, Function<E, Boolean> additionalSlaveVisibilityCondition) {
        slaveVisibleProperty().bind(Properties.compute(masterSelectedEntityProperty, selectedEntity -> selectedEntity != null && (additionalSlaveVisibilityCondition == null || additionalSlaveVisibilityCondition.apply(selectedEntity))));
    }


    /*==================================================================================================================
    =========================================== Slave view builder registry ============================================
    ==================================================================================================================*/

    @FunctionalInterface
    public interface SlaveViewBuilder {
        UiBuilder createAndBindSlaveViewIfApplicable(Object pm, Object mixin, Supplier<Pane> containerGetter);
    }

    private final static List<SlaveViewBuilder> slaveViewBuilders = new ArrayList<>();

    public static void registerSlaveViewBuilder(SlaveViewBuilder slaveViewBuilder) {
        slaveViewBuilders.add(slaveViewBuilder);
    }

    public static UiBuilder createAndBindSlaveViewIfApplicable(Object pm, Object mixin, Supplier<Pane> containerGetter) {
        return slaveViewBuilders.stream().map(svb -> svb.createAndBindSlaveViewIfApplicable(pm, mixin, containerGetter)).filter(Objects::nonNull).findFirst().orElse(null);
    }
}
