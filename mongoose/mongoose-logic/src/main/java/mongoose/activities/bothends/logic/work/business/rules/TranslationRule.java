package mongoose.activities.bothends.logic.work.business.rules;

import mongoose.activities.bothends.logic.work.WorkingDocument;

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
