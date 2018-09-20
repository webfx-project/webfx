package mongooses.core.activities.backend.operations;

import webfx.framework.activity.impl.combinations.domainpresentation.impl.DomainPresentationActivityImpl;

/**
 * @author Bruno Salmon
 */
final class OperationsActivity extends DomainPresentationActivityImpl<OperationsPresentationModel> {

    OperationsActivity() {
        super(OperationsPresentationViewActivity::new, OperationsPresentationLogicActivity::new);
    }
}