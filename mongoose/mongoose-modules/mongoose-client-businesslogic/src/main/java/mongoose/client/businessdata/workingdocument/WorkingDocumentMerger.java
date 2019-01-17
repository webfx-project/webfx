package mongoose.client.businessdata.workingdocument;

import mongoose.shared.businessdata.time.DateTimeRange;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Bruno Salmon
 */
public final class WorkingDocumentMerger {

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
                    final WorkingDocumentLine finalLine = line;
                    /* The fact that it could not be found in the preselected calendar working document is probably due
                     * to a booker change in the accommodation type, so we should remove the initial accommodation line
                     * unless lines don't overlap (ex: Early accommodation and Festival accommodation can coexist) */
                    lines.removeIf(wdl -> wdl.isAccommodation() && wdl.getDateTimeRange().overlaps(finalLine.getDateTimeRange()));
                }
                lines.add(line);
            }
            syncLinesInfo(thisLine, line);
        }
        return new WorkingDocument(wd.getEventAggregate(), wd, lines).applyBusinessRules();
    }

    private static void syncLinesInfo(WorkingDocumentLine wdlSrc, WorkingDocumentLine wdlDst) {
        wdlDst.setDocumentLine(wdlSrc.getDocumentLine());
        wdlDst.setAttendances(wdlSrc.getAttendances());
    }

}
