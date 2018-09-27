package mongooses.core.sharedends.businesslogic.rules;

import mongooses.core.sharedends.businesslogic.workingdocument.WorkingDocument;
import mongooses.core.sharedends.businesslogic.workingdocument.WorkingDocumentLine;
import mongooses.core.shared.entities.Option;

/**
 * @author Bruno Salmon
 */
public abstract class BusinessRule {

    abstract public void apply(WorkingDocument wd);

    static WorkingDocumentLine addNewDependentLine(WorkingDocument wd, Option dependentOption, WorkingDocumentLine masterLine, long shiftDays) {
        WorkingDocumentLine dependantLine = new WorkingDocumentLine(dependentOption, wd, null);
        applySameAttendances(dependantLine, masterLine, shiftDays);
        wd.getWorkingDocumentLines().add(dependantLine);
        return dependantLine;
    }

    static void applySameAttendances(WorkingDocumentLine dependentLine, WorkingDocumentLine masterLine, long shiftDays) {
        dependentLine.setDaysArray(masterLine.getDaysArray().shift(shiftDays));
    }
}
