package mongoose.activities.backend.authorizations;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Node;
import mongoose.services.authn.MongooseUserPrincipal;
import naga.framework.activity.base.elementals.view.impl.ViewDomainActivityBase;
import naga.framework.orm.entity.Entities;
import naga.framework.orm.entity.Entity;
import naga.framework.ui.filter.ReactiveExpressionFilterFactoryMixin;
import naga.framework.ui.layouts.FlexBox;
import naga.fxdata.control.DataGrid;

/**
 * @author Bruno Salmon
 */
class AuthorizationsViewActivity extends ViewDomainActivityBase
        implements ReactiveExpressionFilterFactoryMixin {

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
        createReactiveExpressionFilter("{class: 'AuthorizationManagement'}")
                .combine(userPrincipalProperty(), principal -> "{where: 'manager = " + (principal instanceof MongooseUserPrincipal ? ((MongooseUserPrincipal) principal).getUserPersonId() : null) + "'}")
                .setExpressionColumns("[{label: 'Managee', expression: 'user.<ident>'}]")
                .displayResultInto(usersDataGrid.displayResultProperty())
                .setSelectedEntityHandler(usersDataGrid.displaySelectionProperty(), selectedManagementProperty::setValue)
                .start();

        createReactiveExpressionFilter("{class: 'AuthorizationAssignment'}")
                .combine(selectedManagementProperty, management -> "{where: 'management = " + Entities.getPrimaryKey(management) + "'}")
                .setExpressionColumns("active")
                .displayResultInto(assignmentsDataGrid.displayResultProperty())
                //.setSelectedEntityHandler(pm.genericDisplaySelectionProperty(), organization -> new RouteToEventsRequest(organization, getHistory()).execute() )
                .start();
    }
}