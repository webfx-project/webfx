package mongoose.activities.shared.logic.work.rules;

import mongoose.activities.shared.logic.work.WorkingDocument;

/**
 * @author Bruno Salmon
 */
class TranslationRule extends WorkingDocumentRule {

    @Override
    void apply(WorkingDocument wd) {
        if (!wd.hasTeaching())
            wd.removeTranslationLine();
        else if (wd.hasTranslation())
            applySameAttendances(wd.getTranslationLine(), wd.getTeachingLine(), 0);
    }
}
