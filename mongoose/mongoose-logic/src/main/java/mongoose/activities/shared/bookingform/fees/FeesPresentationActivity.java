package mongoose.activities.shared.bookingform.fees;

import naga.framework.activity.combinations.domainpresentation.impl.DomainPresentationActivityImpl;

/**
 * @author Bruno Salmon
 */
public class FeesPresentationActivity extends DomainPresentationActivityImpl<FeesPresentationModel> {

    public FeesPresentationActivity() {
        super(FeesPresentationViewActivity::new, FeesPresentationLogicActivity::new);
    }
}
