package mongooses.core.activities.sharedends.logic.work.business.rules;

import mongooses.core.activities.sharedends.logic.work.WorkingDocument;

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
