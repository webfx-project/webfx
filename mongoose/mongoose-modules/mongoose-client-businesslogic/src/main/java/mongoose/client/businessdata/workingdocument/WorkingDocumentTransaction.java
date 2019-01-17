package mongoose.client.businessdata.workingdocument;

import mongoose.shared.businessdata.time.DateTimeRange;
import mongoose.shared.businessdata.time.TimeInterval;
import mongoose.shared.entities.Option;
import webfx.platform.shared.util.collection.Collections;

/**
 * A transaction for a working document to be used for a very short time such as reacting to a click in the booking form.
 *
 * @author Bruno Salmon
 */
public final class WorkingDocumentTransaction {

    private final WorkingDocument workingDocument;
    private DateTimeRange initialWorkingDocumentDateTimeRange;

    public WorkingDocumentTransaction(WorkingDocument workingDocument) {
        this.workingDocument = workingDocument;
    }

    public WorkingDocument getWorkingDocument() {
        return workingDocument;
    }

    private DateTimeRange getInitialWorkingDocumentDateTimeRange() {
        if (initialWorkingDocumentDateTimeRange == null)
            initialWorkingDocumentDateTimeRange = new DateTimeRange(workingDocument.getDateTimeRange().getInterval());
        return initialWorkingDocumentDateTimeRange;
    }

    public WorkingDocumentLine addOption(Option option) {
        // Note: getInitialWorkingDocumentDateTimeRange() must be called before to capture the initial date time range if not yet done
        WorkingDocumentLine line = new WorkingDocumentLine(option, workingDocument, getInitialWorkingDocumentDateTimeRange());
        workingDocument.getWorkingDocumentLines().add(line);
        return line;
    }

    public void removeOption(Option option) {
        // Note: getInitialWorkingDocumentDateTimeRange() must be called before to capture the initial date time range if not yet done
        getInitialWorkingDocumentDateTimeRange();
        Collections.removeIf(workingDocument.getWorkingDocumentLines(), wdl -> isOptionBookedInWorkingDocumentLine(wdl, option));
    }

    public boolean isOptionBooked(Option option) {
        return Collections.anyMatch(workingDocument.getWorkingDocumentLines(), wdl -> isOptionBookedInWorkingDocumentLine(wdl, option));
    }

    private static boolean isOptionBookedInWorkingDocumentLine(WorkingDocumentLine wdl, Option option) {
        Option wdlOption = wdl.getOption();
        return wdlOption != null ? wdlOption == option : wdl.getSite() == option.getSite() && wdl.getItem() == option.getItem();
    }

    public void commit() {
        // Calling business rules. As there are possible changes that the working document is not aware of (such as
        // a selected option button but with no new lines added yet - they will be added thanks to business rules),
        workingDocument.markAsChangedForBusinessRules(); // we call this method to force business rules execution.
        workingDocument.applyBusinessRules();
        if (initialWorkingDocumentDateTimeRange != null) {
            // Finally we check if this transaction has changed the working document interval
            TimeInterval workingDocumentInterval = workingDocument.getDateTimeRange().getInterval();
            if (!workingDocumentInterval.equals(initialWorkingDocumentDateTimeRange.getInterval())) {
                // If yes, we should reinitialize the empty lines because they may now be included in the new interval
                // (Ex: adding an arrival airport shuttle option may cause the early lunch be attended by default)
                DateTimeRange intervalDateTimeRange = new DateTimeRange(workingDocumentInterval);
                Collections.forEach(workingDocument.getWorkingDocumentLines(), wdl -> {
                    if (wdl.getDateTimeRange().isEmpty())
                        wdl.initializeDateTimeRange(intervalDateTimeRange);
                });
            }
            initialWorkingDocumentDateTimeRange = null; // For next possible reuse of this transaction instance
        }
    }
}
