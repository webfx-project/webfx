package mongoose.activities.shared.logic.work.transaction;

import mongoose.activities.shared.logic.work.WorkingDocument;
import mongoose.activities.shared.logic.work.WorkingDocumentLine;
import mongoose.entities.Option;
import naga.util.collection.Collections;

import java.util.ArrayList;
import java.util.List;

/**
 * A transaction for a working document to be used for a very short time such as reacting to a click in the booking form.
 * During that transaction, new lines are immediately added to the working document whereas removed lines are postponed
 * to the commit phase. The reason is that when adding a new line, its date time range is deduced from the global working
 * document date range and this should still consider removed lines during that transaction time.
 *
 * For example, when the booker changes the accommodation type selection, this causes the old accommodation line to be
 * removed and a new line with the new selected accommodation type to be added. If the old line contained the arrival or
 * departure (ex: the booker arriving for the night), removing it immediately before adding the new line would result in
 * loosing that arrival or departure night in the new line - which is not what we wished (the accommodation selection
 * change shouldn't affect the booking date time range).
 *
 * @author Bruno Salmon
 */
public class WorkingDocumentTransaction {

    private final WorkingDocument workingDocument;
    private List<WorkingDocumentLine> addedLines; // memorized just for a possible rollback
    private List<WorkingDocumentLine> linesToRemove; // really removed on commit

    public WorkingDocumentTransaction(WorkingDocument workingDocument) {
        this.workingDocument = workingDocument;
    }

    public WorkingDocument getWorkingDocument() {
        return workingDocument;
    }

    private List<WorkingDocumentLine> getAddedLines() {
        if (addedLines == null)
            addedLines = new ArrayList<>();
        return addedLines;
    }

    private List<WorkingDocumentLine> getLinesToRemove() {
        if (linesToRemove == null)
            linesToRemove = new ArrayList<>();
        return linesToRemove;
    }

    public WorkingDocumentLine addOption(Option option) {
        WorkingDocumentLine line = new WorkingDocumentLine(option, workingDocument); // This constructor deduces the
        // line date time range from the working document one (that's why the working document still contains removed
        // lines before the commit)
        addLine(line);
        return line;
    }

    public void removeOption(Option option) {
        List<WorkingDocumentLine> linesToRemove = Collections.filter(workingDocument.getWorkingDocumentLines(), wdl -> isOptionBookedInWorkingDocumentLine(wdl, option));
        Collections.forEach(linesToRemove, this::removeLine);
    }

    public boolean isOptionBooked(Option option) {
        return Collections.hasAtLeastOneMatching(workingDocument.getWorkingDocumentLines(), wdl ->
                isOptionBookedInWorkingDocumentLine(wdl, option) && (linesToRemove == null || !linesToRemove.contains(wdl)));
    }

    private static boolean isOptionBookedInWorkingDocumentLine(WorkingDocumentLine wdl, Option option) {
        Option wdlOption = wdl.getOption();
        return wdlOption != null ? wdlOption == option : wdl.getSite() == option.getSite() && wdl.getItem() == option.getItem();
    }

    public void addLine(WorkingDocumentLine line) {
        getAddedLines().add(line);
        workingDocument.getWorkingDocumentLines().add(line);
    }

    public void removeLine(WorkingDocumentLine line) {
        getLinesToRemove().add(line);
    }

    public void commit() {
        sync(true);
    }

    public void rollback() {
        sync(false);
    }

    private void sync(boolean commit) {
        List<WorkingDocumentLine> lines = workingDocument.getWorkingDocumentLines();
        if (addedLines != null && !commit)
            lines.removeAll(addedLines);
        if (linesToRemove != null && commit)
            lines.removeAll(linesToRemove);
        addedLines = linesToRemove = null;
        workingDocument.applyBusinessRules();
    }
}
