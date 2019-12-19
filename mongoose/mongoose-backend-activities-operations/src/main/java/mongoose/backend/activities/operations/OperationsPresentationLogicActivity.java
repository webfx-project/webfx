package mongoose.backend.activities.operations;

import javafx.scene.layout.Pane;
import mongoose.client.activity.MongooseDomainPresentationLogicActivityBase;
import webfx.framework.client.orm.reactive.mapping.entities_to_visual.ReactiveVisualMapper;
import webfx.framework.client.ui.controls.entity.sheet.EntityPropertiesSheet;
import webfx.framework.shared.orm.entity.Entity;
import webfx.kit.launcher.WebFxKitLauncher;
import webfx.platform.shared.util.function.Factory;

import static webfx.framework.shared.orm.dql.DqlStatement.limit;
import static webfx.framework.shared.orm.dql.DqlStatement.where;

/**
 * @author Bruno Salmon
 */
final class OperationsPresentationLogicActivity
        extends MongooseDomainPresentationLogicActivityBase<OperationsPresentationModel> {

    private final String expressionColumns = "['name','operationCode','i18nCode','backend','frontend','public']";

    OperationsPresentationLogicActivity() {
        this(OperationsPresentationModel::new);
    }

    private OperationsPresentationLogicActivity(Factory<OperationsPresentationModel> presentationModelFactory) {
        super(presentationModelFactory);
    }

    @Override
    protected void startLogic(OperationsPresentationModel pm) {
        ReactiveVisualMapper.createPushReactiveChain(this)
                .always("{class: 'Operation', alias: 'o', orderBy: 'name'}")
                // Search box condition
                .ifTrimNotEmpty(pm.searchTextProperty(), s -> where("lower(name) like ?", "%" + s.toLowerCase() + "%"))
                // Limit condition
                .ifPositive(pm.limitProperty(), l -> limit("?", l))
                .setEntityColumns(expressionColumns)
                .visualizeResultInto(pm.genericVisualResultProperty())
                .setVisualSelectionProperty(pm.genericVisualSelectionProperty())
                .setSelectedEntityHandler(this::editOperation)
                .start()
        ;
    }

    private void editOperation(Entity operation) {
        Pane parent = (Pane) WebFxKitLauncher.getPrimaryStage().getScene().getRoot();
        EntityPropertiesSheet.editEntity(operation, expressionColumns, parent);
    }
}
