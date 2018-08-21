package mongoose.activities.backend.authorizations;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import mongoose.services.authn.MongooseUserPrincipal;
import naga.framework.activity.base.elementals.view.impl.ViewDomainActivityBase;
import naga.framework.orm.entity.Entities;
import naga.framework.orm.entity.Entity;
import naga.framework.ui.filter.ReactiveExpressionFilterFactoryMixin;
import naga.framework.ui.graphic.controls.sheet.PropertySheet;
import naga.framework.ui.layouts.FlexBox;
import naga.fxdata.control.DataGrid;

/**
 * @author Bruno Salmon
 */
final class AuthorizationsViewActivity extends ViewDomainActivityBase
        implements ReactiveExpressionFilterFactoryMixin {

    private final String manageeColumns = "[{label: 'Managee', expression: 'active,user.genderIcon,user.firstName,user.lastName'}]";
    private final String assignmentColumns = "active,operation,rule,activityState";

    private DataGrid usersDataGrid = new DataGrid();
    private DataGrid assignmentsDataGrid = new DataGrid();

    private final ObjectProperty<Entity> selectedManagementProperty = new SimpleObjectProperty<>();

    @Override
    public Node buildUi() {
        FlexBox flexBox = new FlexBox();
        flexBox.getChildren().setAll(usersDataGrid, assignmentsDataGrid);
        return flexBox;
    }


    protected void startLogic() {
        createReactiveExpressionFilter("{class: 'AuthorizationManagement', orderBy: 'id'}")
                .combine(userPrincipalProperty(), principal -> "{where: 'manager = " + MongooseUserPrincipal.getUserPersonId(principal) + "'}")
                .setExpressionColumns(manageeColumns)
                .displayResultInto(usersDataGrid.displayResultProperty())
                .setSelectedEntityHandler(usersDataGrid.displaySelectionProperty(), selectedManagementProperty::setValue)
                .start();

        createReactiveExpressionFilter("{class: 'AuthorizationAssignment', orderBy: 'id'}")
                .combine(selectedManagementProperty, management -> "{where: 'management = " + Entities.getPrimaryKey(management) + "'}")
                .setExpressionColumns(assignmentColumns)
                .displayResultInto(assignmentsDataGrid.displayResultProperty())
                .setSelectedEntityHandler(assignmentsDataGrid.displaySelectionProperty(), assignment -> PropertySheet.editEntity(assignment, assignmentColumns, (Pane) getNode()))
                .start();
    }
}