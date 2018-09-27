package mongooses.core.backend.activities.authorizations;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import mongooses.core.sharedends.services.authn.MongooseUserPrincipal;
import webfx.framework.activity.impl.combinations.viewdomain.impl.ViewDomainActivityBase;
import webfx.framework.orm.entity.Entities;
import webfx.framework.orm.entity.Entity;
import webfx.framework.ui.filter.ReactiveExpressionFilter;
import webfx.framework.ui.filter.ReactiveExpressionFilterFactoryMixin;
import webfx.framework.ui.controls.sheet.PropertySheet;
import webfx.framework.ui.layouts.FlexBox;
import webfx.fxkits.extra.control.DataGrid;

/**
 * @author Bruno Salmon
 */
final class AuthorizationsViewActivity extends ViewDomainActivityBase
        implements ReactiveExpressionFilterFactoryMixin {

    private final String manageeColumns = "[{label: 'Managee', expression: `active,user.genderIcon,user.firstName,user.lastName`}]";
    private final String assignmentColumns = "[`active`,`operation`,{expression: `rule`, foreignFields: null, foreignSearchCondition: null, foreignCondition: null},`activityState`]";

    private final DataGrid usersDataGrid = new DataGrid();
    private final DataGrid assignmentsDataGrid = new DataGrid();

    private final ObjectProperty<Entity> selectedManagementProperty = new SimpleObjectProperty<>();

    private ReactiveExpressionFilter<Entity> assignmentFilter;

    @Override
    public Node buildUi() {
        assignmentsDataGrid.setOnMouseClicked(e -> {
            if (e.getClickCount() == 2)
                PropertySheet.editEntity(assignmentFilter.getSelectedEntity(), assignmentColumns, (Pane) getNode());
        });
        return new FlexBox(usersDataGrid, assignmentsDataGrid);
    }


    protected void startLogic() {
        createReactiveExpressionFilter("{class: 'AuthorizationManagement', orderBy: 'id'}")
                .combine(userPrincipalProperty(), principal -> "{where: 'manager = " + MongooseUserPrincipal.getUserPersonId(principal) + "'}")
                .setExpressionColumns(manageeColumns)
                .displayResultInto(usersDataGrid.displayResultProperty())
                .setSelectedEntityHandler(usersDataGrid.displaySelectionProperty(), selectedManagementProperty::setValue)
                .start();

        assignmentFilter = createReactiveExpressionFilter("{class: 'AuthorizationAssignment', orderBy: 'id'}")
                .combine(selectedManagementProperty, management -> "{where: 'management = " + Entities.getPrimaryKey(management) + "'}")
                .setExpressionColumns(assignmentColumns)
                .displayResultInto(assignmentsDataGrid.displayResultProperty())
                .setDisplaySelectionProperty(assignmentsDataGrid.displaySelectionProperty())
                //.setSelectedEntityHandler(assignmentsDataGrid.displaySelectionProperty(), assignment -> PropertySheet.editEntity(assignment, assignmentColumns, (Pane) getNode()))
                .start();
    }
}