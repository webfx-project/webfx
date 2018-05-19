package mongoose.activities.shared.book.event.terms;

import naga.framework.activity.combinations.domainpresentation.impl.DomainPresentationActivityImpl;

/**
 * @author Bruno Salmon
 */
class TermsActivity extends DomainPresentationActivityImpl<TermsPresentationModel> {

    TermsActivity() {
        super(TermsPresentationViewActivity::new, TermsPresentationLogicActivity::new);
    }
}
