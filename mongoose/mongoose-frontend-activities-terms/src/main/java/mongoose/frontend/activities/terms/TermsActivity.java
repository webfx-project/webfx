package mongoose.frontend.activities.terms;

import webfx.framework.client.activity.impl.combinations.domainpresentation.impl.DomainPresentationActivityImpl;

/**
 * @author Bruno Salmon
 */
final class TermsActivity extends DomainPresentationActivityImpl<TermsPresentationModel> {

    TermsActivity() {
        super(TermsPresentationViewActivity::new, TermsPresentationLogicActivity::new);
    }
}
