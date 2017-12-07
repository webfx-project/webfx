package mongoose.activities.shared.logic.work.rules;

import mongoose.activities.shared.logic.work.WorkingDocument;
import mongoose.entities.Option;

import static mongoose.activities.shared.logic.work.rules.WorkingDocumentRules.isTouristTaxOption;

/**
 * @author Bruno Salmon
 */
class TouristTaxRule extends WorkingDocumentRule {

    @Override
    void apply(WorkingDocument wd) {
        if (!wd.hasAccommodation())
            wd.removeTouristTaxLine();
        else if (!wd.hasTouristTax()) {
            Option touristTaxOption = wd.getEventService().findFirstOption(o -> isTouristTaxOption(o) && (o.hasNoParent() || wd.getAccommodationLine() != null && o.getParent().getItem() == wd.getAccommodationLine().getItem()));
            if (touristTaxOption != null)
                wd.setTouristTaxLine(addNewDependentLine(wd, touristTaxOption, wd.getAccommodationLine(), 0));
        }
    }
}
