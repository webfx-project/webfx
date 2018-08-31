package mongoose.activities.backend.operations;

import webfx.framework.activity.base.combinations.domainpresentation.impl.DomainPresentationActivityImpl;

/**
 * @author Bruno Salmon
 */
final class OperationsActivity extends DomainPresentationActivityImpl<OperationsPresentationModel> {

    OperationsActivity() {
        super(OperationsPresentationViewActivity::new, OperationsPresentationLogicActivity::new);
    }
}