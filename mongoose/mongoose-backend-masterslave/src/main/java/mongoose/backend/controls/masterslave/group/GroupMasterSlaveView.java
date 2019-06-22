package mongoose.backend.controls.masterslave.group;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Node;
import mongoose.backend.controls.masterslave.MasterSlaveView;
import mongoose.backend.controls.masterslave.MasterTableView;
import mongoose.backend.controls.masterslave.SlaveTableView;
import mongoose.backend.controls.masterslave.UiBuilder;
import mongoose.client.presentationmodel.*;
import webfx.framework.client.ui.controls.ControlFactoryMixin;
import webfx.framework.client.ui.filter.StringFilter;
import webfx.fxkit.util.properties.Properties;
import webfx.platform.shared.util.Strings;

import java.util.function.Function;

public class GroupMasterSlaveView extends MasterSlaveView {

    public GroupMasterSlaveView() {
        this((Node) null, null, null);
    }

    public GroupMasterSlaveView(UiBuilder groupView, UiBuilder masterView, UiBuilder slaveView) {
        this(groupView.buildUi(), masterView.buildUi(), slaveView.buildUi());
    }

    public GroupMasterSlaveView(Node groupView, Node masterView, Node slaveView) {
        super(masterView, slaveView);
        setGroupView(groupView);
        setMasterVisible(true);
    }

    private final ObjectProperty<Node> groupViewProperty = new SimpleObjectProperty<Node/*GWT*/>() {
        @Override
        protected void invalidated() {
            updateView();
        }
    };

    public ObjectProperty<Node> groupViewProperty() {
        return groupViewProperty;
    }

    public void setGroupView(Node groupView) {
        groupViewProperty().set(groupView);
    }

    public Node getGroupView() {
        return groupViewProperty().get();
    }

    @Override
    protected void updateView() {
        updateSplitPane(isGroupVisible() ? getGroupView() : null, isMasterVisible() ? getMasterView() : null, isSlaveVisible() ? getSlaveView() : null);
    }

    private final BooleanProperty groupVisibleProperty = new SimpleBooleanProperty() {
        @Override
        protected void invalidated() {
            updateView();
        }
    };

    public BooleanProperty groupVisibleProperty() {
        return groupVisibleProperty;
    }

    public boolean isGroupVisible() {
        return groupVisibleProperty().get();
    }

    public void setGroupVisible(boolean groupVisible) {
        groupVisibleProperty().setValue(groupVisible);
    }

    private final BooleanProperty masterVisibleProperty = new SimpleBooleanProperty() {
        @Override
        protected void invalidated() {
            updateView();
        }
    };

    public BooleanProperty masterVisibleProperty() {
        return masterVisibleProperty;
    }

    public boolean isMasterVisible() {
        return masterVisibleProperty().get();
    }

    public void setMasterVisible(boolean masterVisible) {
        masterVisibleProperty().setValue(masterVisible);
    }


    /*==================================================================================================================
    =================================================== Data binding ===================================================
    ==================================================================================================================*/

    @Override
    public <E> void doVisibilityBinding(GroupView groupView, ObjectProperty<E> masterSelectedEntityProperty, Function<E, Boolean> additionalSlaveVisibilityCondition) {
        groupVisibleProperty() .bind(Properties.compute(groupView.groupStringFilterProperty(), s -> s != null && Strings.isNotEmpty(new StringFilter(s).getGroupBy())));
        masterVisibleProperty().bind(Properties.combine(groupVisibleProperty(),  groupView.selectedGroupProperty(), (groupVisible, selectedGroup) -> !groupVisible || selectedGroup != null));
        slaveVisibleProperty() .bind(Properties.combine(masterVisibleProperty(), masterSelectedEntityProperty, (masterVisible, selectedEntity) -> masterVisible && selectedEntity != null && (additionalSlaveVisibilityCondition == null || additionalSlaveVisibilityCondition.apply(selectedEntity))));
    }


    /*==================================================================================================================
    ============================================== Static factory methods ==============================================
    ==================================================================================================================*/

    public static <PM extends HasGroupDisplayResultProperty & HasMasterDisplayResultProperty & HasSlaveDisplayResultProperty & HasSelectedMasterProperty>
    GroupMasterSlaveView createAndBind(PM pm, ControlFactoryMixin mixin) {
        return createAndBind(SlaveTableView.createAndBind(pm), pm, mixin);
    }

    public static <PM extends HasGroupDisplayResultProperty & HasMasterDisplayResultProperty & HasSelectedMasterProperty>
    GroupMasterSlaveView createAndBind(UiBuilder slaveUiFactory, PM pm, ControlFactoryMixin mixin) {
        return createAndBind(MasterTableView.createAndBind(pm, mixin), slaveUiFactory, pm);
    }

    public static <PM extends HasGroupDisplayResultProperty & HasSelectedMasterProperty>
    GroupMasterSlaveView createAndBind(UiBuilder masterUiFactory, UiBuilder slaveUiFactory, PM pm) {
        return createAndBind(
                GroupView.createAndBind(pm),
                masterUiFactory,
                slaveUiFactory,
                pm);
    }

    public static GroupMasterSlaveView createAndBind(GroupView groupView, UiBuilder masterView, UiBuilder slaveView, HasSelectedMasterProperty pm) {
        return createAndBind(groupView, masterView.buildUi(), slaveView.buildUi(), pm);
    }

    public static GroupMasterSlaveView createAndBind(GroupView groupView, Node masterView, Node slaveView, HasSelectedMasterProperty pm) {
        return createAndBind(groupView, masterView, slaveView, pm.selectedMasterProperty(), pm instanceof HasSlaveVisibilityCondition ? selectedMaster -> ((HasSlaveVisibilityCondition) pm).isSlaveVisible(selectedMaster) : null);
    }

    public static <E> GroupMasterSlaveView createAndBind(GroupView groupView, Node masterView, Node slaveView, ObjectProperty<E> masterSelectedEntityProperty, Function<E, Boolean> additionalSlaveVisibilityCondition) {
        GroupMasterSlaveView groupMasterSlaveView = new GroupMasterSlaveView(groupView.buildUi(), masterView, slaveView);
        groupMasterSlaveView.doVisibilityBinding(groupView, masterSelectedEntityProperty, additionalSlaveVisibilityCondition);
        return groupMasterSlaveView;
    }
}
