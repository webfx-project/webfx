package mongooses.core.activities.sharedends.logic.work.business;

import mongooses.core.activities.sharedends.logic.work.WorkingDocument;
import mongooses.core.activities.sharedends.logic.work.WorkingDocumentLine;
import mongooses.core.activities.sharedends.logic.work.business.logic.OptionLogic;
import mongooses.core.entities.Option;
import webfx.platforms.core.util.collection.Collections;
import java.util.function.Predicate;

import java.util.Collection;
import java.util.List;

/**
 * @author Bruno Salmon
 */
public class BusinessLines {

    private final BusinessType businessType;
    private final WorkingDocument workingDocument;
    private List<WorkingDocumentLine> businessWorkingDocumentLines;

    public BusinessLines(BusinessType businessType, WorkingDocument workingDocument) {
        this.businessType = businessType;
        this.workingDocument = workingDocument;
    }

    public List<WorkingDocumentLine> getBusinessWorkingDocumentLines() {
        if (businessWorkingDocumentLines == null)
            businessWorkingDocumentLines = findBusinessWorkingLines();
        return businessWorkingDocumentLines;
    }

    public boolean isEmpty() {
        //return getBusinessWorkingDocumentLines().isEmpty();
        return Collections.noneMatch(getBusinessWorkingDocumentLines(), wdl -> !wdl.getDaysArray().isEmpty());
    }

    public void removeAllLines() {
        removeLines(getBusinessWorkingDocumentLines());
    }

    public void removeLines(Collection<WorkingDocumentLine> lines) {
        if (lines != null) {
            workingDocument.getWorkingDocumentLines().removeAll(lines);
            businessWorkingDocumentLines = null;
        }
    }

    private List<WorkingDocumentLine> findBusinessWorkingLines() {
        switch (businessType) {
            case TEACHING: return findOptionLines(Option::isTeaching);
            case TRANSLATION: return findOptionLines(Option::isTranslation);
            case ACCOMMODATION: return findBusinessWorkingLines(WorkingDocumentLine::isAccommodation);
            case BREAKFAST: return findOptionLines(OptionLogic::isBreakfastOption);
            case LUNCH: return findOptionLines(OptionLogic::isLunchOption);
            case SUPPER: return findOptionLines(OptionLogic::isSupperOption);
            case DIET: return findBusinessWorkingLines(WorkingDocumentLine::isDiet);
            case TOURIST_TAX: return findOptionLines(OptionLogic::isTouristTaxOption);
            case HOTEL_SHUTTLE: return findOptionLines(OptionLogic::isHotelShuttleOption);
        }
        return null; // shouldn't happen
    }

    private List<WorkingDocumentLine> findBusinessWorkingLines(Predicate<WorkingDocumentLine> predicate) {
        return Collections.filter(workingDocument.getWorkingDocumentLines(), predicate);
    }

    private List<WorkingDocumentLine> findOptionLines(Predicate<Option> predicate) {
        return findBusinessWorkingLines(wdl -> predicate.test(wdl.getOption()));
    }

}
