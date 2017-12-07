package mongoose.activities.shared.logic.work.business.rules;

import mongoose.activities.shared.logic.work.WorkingDocument;

/**
 * @author Bruno Salmon
 */
public class TranslationRule extends BusinessRule {

    @Override
    public void apply(WorkingDocument wd) {
        if (!wd.hasTeaching())
            wd.removeTranslation();
        else if (wd.hasTranslation())
            applySameAttendances(wd.getTranslationLine(), wd.getTeachingLine(), 0);
    }
}
