package mongoose.backend.activities.authorizations;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Node;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.Pane;
import mongoose.client.services.authn.MongooseUserPrincipal;
import webfx.extras.visual.controls.grid.VisualGrid;
import webfx.framework.client.activity.impl.combinations.viewdomain.impl.ViewDomainActivityBase;
import webfx.framework.client.orm.entity.filter.visual.ReactiveVisualFilter;
import webfx.framework.client.orm.entity.filter.visual.ReactiveVisualFilterFactoryMixin;
import webfx.framework.client.ui.controls.sheet.EntityPropertiesSheet;
import webfx.framework.shared.orm.entity.Entities;
import webfx.framework.shared.orm.entity.Entity;

import static webfx.framework.client.orm.dql.DqlStatement.where;

/**
 * @author Bruno Salmon
 */
final class AuthorizationsViewActivity extends ViewDomainActivityBase
        implements ReactiveVisualFilterFactoryMixin {

    private final String manageeColumns = "[{label: 'Managee', expression: `active,user.genderIcon,user.firstName,user.lastName`}]";
    private final String assignmentColumns = "[`active`,`operation`,{expression: `rule`, foreignColumns: null, foreignSearchCondition: null, foreignWhere: null},`activityState`]";

    private final VisualGrid usersGrid = new VisualGrid();
    private final VisualGrid assignmentsGrid = new VisualGrid();

    private final ObjectProperty<Entity> selectedManagementProperty = new SimpleObjectProperty<>();

    private ReactiveVisualFilter<Entity> assignmentFilter;

    @Override
    public Node buildUi() {
        assignmentsGrid.setOnMouseClicked(e -> {
            if (e.getClickCount() == 2)
                EntityPropertiesSheet.editEntity(assignmentFilter.getSelectedEntity(), assignmentColumns, (Pane) getNode());
        });
        return new SplitPane(usersGrid, assignmentsGrid);
    }


    protected void startLogic() {
        createReactiveVisualFilter("{class: 'AuthorizationManagement', orderBy: 'id'}")
                .combine(userPrincipalProperty(), principal -> where("manager=?", MongooseUserPrincipal.getUserPersonId(principal)))
                .setEntityColumns(manageeColumns)
                .visualizeResultInto(usersGrid.visualResultProperty())
                .setSelectedEntityHandler(usersGrid.visualSelectionProperty(), selectedManagementProperty::setValue)
                .start();

        assignmentFilter = createReactiveVisualFilter("{class: 'AuthorizationAssignment', orderBy: 'id'}")
                .combine(selectedManagementProperty, management -> where("management=?", Entities.getPrimaryKey(management)))
                .setEntityColumns(assignmentColumns)
                .visualizeResultInto(assignmentsGrid.visualResultProperty())
                .setVisualSelectionProperty(assignmentsGrid.visualSelectionProperty())
                //.setSelectedEntityHandler(assignmentsDataGrid.visualSelectionProperty(), assignment -> EntityPropertiesSheet.editEntity(assignment, assignmentColumns, (Pane) getNode()))
                .start();
    }
}