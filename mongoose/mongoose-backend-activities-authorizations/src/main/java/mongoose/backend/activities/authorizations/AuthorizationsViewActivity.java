package mongoose.backend.activities.authorizations;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Node;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.Pane;
import mongoose.client.services.authn.MongooseUserPrincipal;
import webfx.framework.client.activity.impl.combinations.viewdomain.impl.ViewDomainActivityBase;
import webfx.framework.client.ui.controls.sheet.EntityPropertiesSheet;
import webfx.framework.client.ui.filter.ReactiveExpressionFilter;
import webfx.framework.client.ui.filter.ReactiveExpressionFilterFactoryMixin;
import webfx.framework.shared.orm.entity.Entities;
import webfx.framework.shared.orm.entity.Entity;
import webfx.extras.visual.controls.grid.VisualGrid;

/**
 * @author Bruno Salmon
 */
final class AuthorizationsViewActivity extends ViewDomainActivityBase
        implements ReactiveExpressionFilterFactoryMixin {

    private final String manageeColumns = "[{label: 'Managee', expression: `active,user.genderIcon,user.firstName,user.lastName`}]";
    private final String assignmentColumns = "[`active`,`operation`,{expression: `rule`, foreignColumns: null, foreignSearchCondition: null, foreignWhere: null},`activityState`]";

    private final VisualGrid usersVisualGrid = new VisualGrid();
    private final VisualGrid assignmentsVisualGrid = new VisualGrid();

    private final ObjectProperty<Entity> selectedManagementProperty = new SimpleObjectProperty<>();

    private ReactiveExpressionFilter<Entity> assignmentFilter;

    @Override
    public Node buildUi() {
        assignmentsVisualGrid.setOnMouseClicked(e -> {
            if (e.getClickCount() == 2)
                EntityPropertiesSheet.editEntity(assignmentFilter.getSelectedEntity(), assignmentColumns, (Pane) getNode());
        });
        return new SplitPane(usersVisualGrid, assignmentsVisualGrid);
    }


    protected void startLogic() {
        createReactiveExpressionFilter("{class: 'AuthorizationManagement', orderBy: 'id'}")
                .combine(userPrincipalProperty(), principal -> "{where: 'manager = " + MongooseUserPrincipal.getUserPersonId(principal) + "'}")
                .setExpressionColumns(manageeColumns)
                .visualizeResultInto(usersVisualGrid.visualResultProperty())
                .setSelectedEntityHandler(usersVisualGrid.visualSelectionProperty(), selectedManagementProperty::setValue)
                .start();

        assignmentFilter = createReactiveExpressionFilter("{class: 'AuthorizationAssignment', orderBy: 'id'}")
                .combine(selectedManagementProperty, management -> "{where: 'management = " + Entities.getPrimaryKey(management) + "'}")
                .setExpressionColumns(assignmentColumns)
                .visualizeResultInto(assignmentsVisualGrid.visualResultProperty())
                .setVisualSelectionProperty(assignmentsVisualGrid.visualSelectionProperty())
                //.setSelectedEntityHandler(assignmentsDataGrid.visualSelectionProperty(), assignment -> EntityPropertiesSheet.editEntity(assignment, assignmentColumns, (Pane) getNode()))
                .start();
    }
}