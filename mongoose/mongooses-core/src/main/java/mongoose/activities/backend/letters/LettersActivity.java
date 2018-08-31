package mongoose.activities.backend.letters;

import webfx.framework.activity.base.combinations.domainpresentation.impl.DomainPresentationActivityImpl;

/**
 * @author Bruno Salmon
 */
final class LettersActivity extends DomainPresentationActivityImpl<LettersPresentationModel> {

    LettersActivity() {
        super(LettersPresentationViewActivity::new, LettersPresentationLogicActivity::new);
    }
}
