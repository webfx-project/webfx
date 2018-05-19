package mongoose.activities.backend.letters;

import naga.framework.activity.combinations.domainpresentation.impl.DomainPresentationActivityImpl;

/**
 * @author Bruno Salmon
 */
class LettersActivity extends DomainPresentationActivityImpl<LettersPresentationModel> {

    LettersActivity() {
        super(LettersPresentationViewActivity::new, LettersPresentationLogicActivity::new);
    }
}
