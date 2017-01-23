package mongoose.activities.shared.bookingform.terms;

import naga.framework.activity.combinations.domainpresentation.impl.DomainPresentationActivityImpl;

/**
 * @author Bruno Salmon
 */
public class TermsPresentationActivity extends DomainPresentationActivityImpl<TermsPresentationModel> {

    public TermsPresentationActivity() {
        super(TermsPresentationViewActivity::new, TermsPresentationLogicActivity::new);
    }
}
