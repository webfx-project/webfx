package mongoose.backend.activities.authorizations;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Node;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.Pane;
import mongoose.client.services.authn.MongooseUserPrincipal;
import webfx.extras.visual.controls.grid.VisualGrid;
import webfx.framework.client.activity.impl.combinations.viewdomain.impl.ViewDomainActivityBase;
import webfx.framework.client.orm.reactive.mapping.entities_to_visual.ReactiveVisualMapper;
import webfx.framework.client.ui.controls.sheet.EntityPropertiesSheet;
import webfx.framework.shared.orm.entity.Entities;
import webfx.framework.shared.orm.entity.Entity;

import static webfx.framework.client.orm.dql.DqlStatement.where;

/**
 * @author Bruno Salmon
 */
final class AuthorizationsViewActivity extends ViewDomainActivityBase {

    private final String manageeColumns = "[{label: 'Managee', expression: `active,user.genderIcon,user.firstName,user.lastName`}]";
    private final String assignmentColumns = "[`active`,`operation`,{expression: `rule`, foreignColumns: null, foreignSearchCondition: null, foreignWhere: null},`activityState`]";

    private final VisualGrid usersGrid = new VisualGrid();
    private final VisualGrid assignmentsGrid = new VisualGrid();

    private final ObjectProperty<Entity> selectedManagementProperty = new SimpleObjectProperty<>();

    @Override
    public Node buildUi() {
        assignmentsGrid.setOnMouseClicked(e -> {
            if (e.getClickCount() == 2)
                EntityPropertiesSheet.editEntity(assignmentVisualMapper.getSelectedEntity(), assignmentColumns, (Pane) getNode());
        });
        return new SplitPane(usersGrid, assignmentsGrid);
    }


    private ReactiveVisualMapper<Entity> assignmentVisualMapper;

    protected void startLogic() {

        ReactiveVisualMapper.createReactiveChain(this)
                .always("{class: 'AuthorizationManagement', orderBy: 'id'}")
                .ifNotNullOtherwiseForceEmpty(userPrincipalProperty(), principal -> where("manager=?", MongooseUserPrincipal.getUserPersonId(principal)))
                .setEntityColumns(manageeColumns)
                .visualizeResultInto(usersGrid.visualResultProperty())
                .setVisualSelectionProperty(usersGrid.visualSelectionProperty())
                .setSelectedEntityHandler(selectedManagementProperty::setValue)
                .start();

        assignmentVisualMapper = ReactiveVisualMapper.createReactiveChain(this)
                .always("{class: 'AuthorizationAssignment', orderBy: 'id'}")
                .ifNotNullOtherwiseForceEmpty(selectedManagementProperty, management -> where("management=?", Entities.getPrimaryKey(management)))
                .setEntityColumns(assignmentColumns)
                .visualizeResultInto(assignmentsGrid.visualResultProperty())
                .setVisualSelectionProperty(assignmentsGrid.visualSelectionProperty())
                .start();
    }
}