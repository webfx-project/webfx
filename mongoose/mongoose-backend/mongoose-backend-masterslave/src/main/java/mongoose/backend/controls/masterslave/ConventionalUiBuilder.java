package mongoose.backend.controls.masterslave;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import mongoose.backend.controls.masterslave.group.GroupMasterSlaveView;
import mongoose.client.entities.util.filters.FilterButtonSelectorFactoryMixin;
import mongoose.client.entities.util.filters.FilterSearchBar;
import webfx.framework.client.orm.reactive.dql.statement.conventions.HasSelectedMasterProperty;
import webfx.framework.client.orm.reactive.mapping.entities_to_visual.conventions.HasGroupVisualResultProperty;
import webfx.framework.client.orm.reactive.mapping.entities_to_visual.conventions.HasMasterVisualResultProperty;
import webfx.framework.client.ui.controls.ControlFactoryMixin;
import webfx.framework.shared.orm.entity.Entity;

import static webfx.framework.client.ui.util.layout.LayoutUtil.setHGrowable;

public class ConventionalUiBuilder implements UiBuilder {

    private final String activityName;
    private final String domainClassId;
    private final Object pm;
    private final ControlFactoryMixin mixin;
    private Node[] leftTopNodes = {}, rightTopNodes = {};
    private FilterSearchBar filterSearchBar; // Keeping this reference for activity resume
    private GroupMasterSlaveView groupMasterSlaveView;
    private BorderPane container;

    private ConventionalUiBuilder(String activityName, String domainClassId, Object pm, ControlFactoryMixin mixin) {
        this.activityName = activityName;
        this.domainClassId = domainClassId;
        this.pm = pm;
        this.mixin = mixin;
    }

    public void setLeftTopNodes(Node... leftTopNodes) {
        this.leftTopNodes = leftTopNodes;
    }

    public void setRightTopNodes(Node... rightTopNodes) {
        this.rightTopNodes = rightTopNodes;
    }

    @Override
    public Pane buildUi() {
        if (container == null) {
            container = new BorderPane();

            // Building the filter search bar and put it on top
            if (mixin instanceof FilterButtonSelectorFactoryMixin) {
                filterSearchBar = ((FilterButtonSelectorFactoryMixin) mixin).createFilterSearchBar(activityName, domainClassId, container, pm);
                if (leftTopNodes.length == 0 && rightTopNodes.length == 0)
                    container.setTop(filterSearchBar.buildUi());
                else {
                    HBox hbox = new HBox(10, leftTopNodes);
                    hbox.setAlignment(Pos.CENTER_LEFT);
                    hbox.getChildren().add(setHGrowable(filterSearchBar.buildUi()));
                    hbox.getChildren().addAll(rightTopNodes);
                    container.setTop(hbox);
                }
            }

            if (pm instanceof HasGroupVisualResultProperty && pm instanceof HasMasterVisualResultProperty && pm instanceof HasSelectedMasterProperty) {
                groupMasterSlaveView = GroupMasterSlaveView.createAndBind((HasGroupVisualResultProperty & HasMasterVisualResultProperty & HasSelectedMasterProperty<Entity/*necessary for GWT 2.9*/>) pm, mixin, () -> container);
                container.setCenter(groupMasterSlaveView.buildUi());
            }
        }

        return container;
    }

    public void onResume() {
        if (filterSearchBar != null)
            filterSearchBar.onResume();
    }

    public GroupMasterSlaveView getGroupMasterSlaveView() {
        return groupMasterSlaveView;
    }

    /*==================================================================================================================
    ============================================== Static factory methods ==============================================
    ==================================================================================================================*/

    public static <PM extends HasGroupVisualResultProperty & HasMasterVisualResultProperty & HasSelectedMasterProperty>
    ConventionalUiBuilder createAndBindGroupMasterSlaveViewWithFilterSearchBar(PM pm, FilterButtonSelectorFactoryMixin mixin, String activityName, String domainClassId) {
        return new ConventionalUiBuilder(activityName, domainClassId, pm, mixin);
    }
}
