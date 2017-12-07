package mongoose.activities.shared.logic.work.rules;

import mongoose.activities.shared.logic.work.WorkingDocument;
import mongoose.activities.shared.logic.work.WorkingDocumentLine;
import mongoose.entities.Option;

/**
 * @author Bruno Salmon
 */
abstract class WorkingDocumentRule {

    abstract void apply(WorkingDocument wd);

    static WorkingDocumentLine addNewDependentLine(WorkingDocument wd, Option dependentOption, WorkingDocumentLine masterLine, long shiftDays) {
        WorkingDocumentLine dependantLine = new WorkingDocumentLine(dependentOption, wd);
        wd.getWorkingDocumentLines().add(dependantLine);
        applySameAttendances(dependantLine, masterLine, shiftDays);
        return dependantLine;
    }

    static void applySameAttendances(WorkingDocumentLine dependentLine, WorkingDocumentLine masterLine, long shiftDays) {
        dependentLine.setDaysArray(masterLine.getDaysArray().shift(shiftDays));
    }



}
