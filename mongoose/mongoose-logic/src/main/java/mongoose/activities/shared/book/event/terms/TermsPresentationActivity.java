package mongoose.activities.shared.book.event.terms;

import naga.framework.activity.combinations.domainpresentation.impl.DomainPresentationActivityImpl;

/**
 * @author Bruno Salmon
 */
class TermsPresentationActivity extends DomainPresentationActivityImpl<TermsPresentationModel> {

    TermsPresentationActivity() {
        super(TermsPresentationViewActivity::new, TermsPresentationLogicActivity::new);
    }
}
