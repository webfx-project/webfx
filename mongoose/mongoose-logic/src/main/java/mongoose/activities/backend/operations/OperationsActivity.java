package mongoose.activities.backend.operations;

import naga.framework.activity.base.combinations.domainpresentation.impl.DomainPresentationActivityImpl;

/**
 * @author Bruno Salmon
 */
class OperationsActivity extends DomainPresentationActivityImpl<OperationsPresentationModel> {

    OperationsActivity() {
        super(OperationsPresentationViewActivity::new, OperationsPresentationLogicActivity::new);
    }
}