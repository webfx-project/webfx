package mongoose.activities.shared.logic.work.merge;

import mongoose.activities.shared.logic.time.DateTimeRange;
import mongoose.activities.shared.logic.work.WorkingDocument;
import mongoose.activities.shared.logic.work.WorkingDocumentLine;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Bruno Salmon
 */
public class WorkingDocumentMerger {

    public static WorkingDocument mergeWorkingDocuments(WorkingDocument wd, WorkingDocument calendarWorkingDocument, DateTimeRange dateTimeRange) {
        List<WorkingDocumentLine> lines = new ArrayList<>(calendarWorkingDocument.getWorkingDocumentLines());
        // If the calendar working document (coming from the preselected options) has accommodation but not the current
        // working document (because the booker probably unselected it), we should not reestablish it
        if (!wd.hasAccommodation())
            lines.removeIf(WorkingDocumentLine::isAccommodation);
        // Same thing with meals
        if (!wd.hasMeals())
            lines.removeIf(WorkingDocumentLine::isMeals);
        for (WorkingDocumentLine thisLine : wd.getWorkingDocumentLines()) {
            WorkingDocumentLine line = calendarWorkingDocument.findSameWorkingDocumentLine(thisLine);
            if (line == null) {
                line = new WorkingDocumentLine(thisLine, dateTimeRange);
                if (line.isAccommodation()) {
                    /* The fact that it could not be found in the preselected calendar working document is probably due
                     * to a booker change in the accommodation type, so we should remove the initial accommodation option */
                    lines.removeIf(WorkingDocumentLine::isAccommodation);
                }
                lines.add(line);
            }
            syncLinesInfo(thisLine, line);
        }
        return new WorkingDocument(wd.getEventService(), wd, lines).applyBusinessRules();
    }

    private static void syncLinesInfo(WorkingDocumentLine wdlSrc, WorkingDocumentLine wdlDst) {
        wdlDst.setDocumentLine(wdlSrc.getDocumentLine());
        wdlDst.setAttendances(wdlSrc.getAttendances());
    }

}
