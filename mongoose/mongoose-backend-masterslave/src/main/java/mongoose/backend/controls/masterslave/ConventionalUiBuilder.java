package mongoose.backend.controls.masterslave;

import javafx.scene.Node;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import mongoose.backend.controls.masterslave.group.GroupMasterSlaveView;
import mongoose.client.entities.util.filters.FilterButtonSelectorFactoryMixin;
import mongoose.client.entities.util.filters.FilterSearchBar;
import mongoose.client.presentationmodel.HasGroupDisplayResultProperty;
import mongoose.client.presentationmodel.HasMasterDisplayResultProperty;
import mongoose.client.presentationmodel.HasSelectedMasterProperty;
import webfx.framework.client.ui.controls.ControlFactoryMixin;

import static webfx.framework.client.ui.layouts.LayoutUtil.setHGrowable;

public class ConventionalUiBuilder implements UiBuilder {

    private final String activityName;
    private final String domainClassId;
    private final Object pm;
    private final ControlFactoryMixin mixin;
    private Node[] leftTopNodes = {}, rightTopNodes = {};
    private FilterSearchBar filterSearchBar; // Keeping this reference for activity resume

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
    public Node buildUi() {
        BorderPane container = new BorderPane();

        // Building the filter search bar and put it on top
        if (mixin instanceof FilterButtonSelectorFactoryMixin) {
            filterSearchBar = ((FilterButtonSelectorFactoryMixin) mixin).createFilterSearchBar(activityName, domainClassId, container, pm);
            if (leftTopNodes.length == 0 && rightTopNodes.length == 0)
                container.setTop(filterSearchBar.buildUi());
            else {
                HBox hbox = new HBox(10, leftTopNodes);
                hbox.getChildren().add(setHGrowable(filterSearchBar.buildUi()));
                hbox.getChildren().addAll(rightTopNodes);
                container.setTop(hbox);
            }
        }

        if (pm instanceof HasGroupDisplayResultProperty && pm instanceof HasMasterDisplayResultProperty && pm instanceof HasSelectedMasterProperty)
            container.setCenter(GroupMasterSlaveView.createAndBind((HasGroupDisplayResultProperty & HasMasterDisplayResultProperty & HasSelectedMasterProperty) pm, mixin, container).buildUi());

        return container;
    }

    public void onResume() {
        if (filterSearchBar != null)
            filterSearchBar.onResume();
    }


    /*==================================================================================================================
    ============================================== Static factory methods ==============================================
    ==================================================================================================================*/

    public static <PM extends HasGroupDisplayResultProperty & HasMasterDisplayResultProperty & HasSelectedMasterProperty>
    ConventionalUiBuilder createAndBindGroupMasterSlaveViewWithFilterSearchBar(PM pm, FilterButtonSelectorFactoryMixin mixin, String activityName, String domainClassId) {
        return new ConventionalUiBuilder(activityName, domainClassId, pm, mixin);
    }
}
