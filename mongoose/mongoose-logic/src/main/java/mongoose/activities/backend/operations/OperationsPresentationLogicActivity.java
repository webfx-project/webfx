package mongoose.activities.backend.operations;

import mongoose.activities.bothends.generic.MongooseDomainPresentationLogicActivityBase;
import naga.framework.ui.filter.ReactiveExpressionFilterFactoryMixin;
import naga.util.function.Factory;

/**
 * @author Bruno Salmon
 */
class OperationsPresentationLogicActivity
        extends MongooseDomainPresentationLogicActivityBase<OperationsPresentationModel>
        implements ReactiveExpressionFilterFactoryMixin {

    OperationsPresentationLogicActivity() {
        this(OperationsPresentationModel::new);
    }

    private OperationsPresentationLogicActivity(Factory<OperationsPresentationModel> presentationModelFactory) {
        super(presentationModelFactory);
    }

    @Override
    protected void startLogic(OperationsPresentationModel pm) {
        // Loading the domain model and setting up the reactive filter
        createReactiveExpressionFilter("{class: 'Operation', alias: 'o'}")
                // Search box condition
                .combineTrimIfNotEmpty(pm.searchTextProperty(), s -> "{where: 'lower(name) like `%" + s.toLowerCase() + "%`'}")
                // Limit condition
                .combineIfPositive(pm.limitProperty(), l -> "{limit: '" + l + "'}")
                .setExpressionColumns("['name','operationCode','i18nCode','backend','frontend','public']")
                .displayResultInto(pm.genericDisplayResultProperty())
                .start();
    }
}
