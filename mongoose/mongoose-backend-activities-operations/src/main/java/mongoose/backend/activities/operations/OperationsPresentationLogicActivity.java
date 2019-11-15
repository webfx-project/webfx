package mongoose.backend.activities.operations;

import javafx.scene.layout.Pane;
import mongoose.client.activity.MongooseDomainPresentationLogicActivityBase;
import webfx.framework.client.ui.controls.sheet.EntityPropertiesSheet;
import webfx.framework.client.orm.entity.filter.visual.ReactiveVisualFilterFactoryMixin;
import webfx.framework.shared.orm.entity.Entity;
import webfx.kit.launcher.WebFxKitLauncher;
import webfx.platform.shared.util.function.Factory;

/**
 * @author Bruno Salmon
 */
final class OperationsPresentationLogicActivity
        extends MongooseDomainPresentationLogicActivityBase<OperationsPresentationModel>
        implements ReactiveVisualFilterFactoryMixin {

    private final String expressionColumns = "['name','operationCode','i18nCode','backend','frontend','public']";

    OperationsPresentationLogicActivity() {
        this(OperationsPresentationModel::new);
    }

    private OperationsPresentationLogicActivity(Factory<OperationsPresentationModel> presentationModelFactory) {
        super(presentationModelFactory);
    }

    @Override
    protected void startLogic(OperationsPresentationModel pm) {
        // Loading the domain model and setting up the reactive filter
        createReactiveVisualFilter("{class: 'Operation', alias: 'o', orderBy: 'name'}")
                // Search box condition
                .combineIfNotEmptyTrim(pm.searchTextProperty(), s -> "{where: 'lower(name) like `%" + s.toLowerCase() + "%`'}")
                // Limit condition
                .combineIfPositive(pm.limitProperty(), l -> "{limit: '" + l + "'}")
                .setEntityColumns(expressionColumns)
                .visualizeResultInto(pm.genericVisualResultProperty())
                .setSelectedEntityHandler(pm.genericVisualSelectionProperty(), this::editOperation)
                .setPush(true)
                .start();
    }

    private void editOperation(Entity operation) {
        Pane parent = (Pane) WebFxKitLauncher.getPrimaryStage().getScene().getRoot();
        EntityPropertiesSheet.editEntity(operation, expressionColumns, parent);
    }
}
