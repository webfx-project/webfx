package mongoose.activities.shared.logic.work;

import mongoose.activities.shared.logic.work.businesslogic.OptionLogic;
import mongoose.entities.Option;
import naga.util.collection.Collections;
import naga.util.function.Predicate;

import java.util.List;

/**
 * @author Bruno Salmon
 */
public class BusinessLines {

    private final BusinessType businessType;
    private final WorkingDocument workingDocument;
    private List<WorkingDocumentLine> businessWorkingDocumentLines;

    BusinessLines(BusinessType businessType, WorkingDocument workingDocument) {
        this.businessType = businessType;
        this.workingDocument = workingDocument;
    }

    public List<WorkingDocumentLine> getBusinessWorkingDocumentLines() {
        if (businessWorkingDocumentLines == null)
            businessWorkingDocumentLines = findBusinessWorkingLines();
        return businessWorkingDocumentLines;
    }

    public boolean isEmpty() {
        return getBusinessWorkingDocumentLines().isEmpty();
    }

    public void removeLines() {
        workingDocument.getWorkingDocumentLines().removeAll(getBusinessWorkingDocumentLines());
        businessWorkingDocumentLines = null;
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
