package mongoose.activities.shared.logic.work.businesslogic.rules;

import mongoose.activities.shared.logic.work.WorkingDocument;
import mongoose.entities.Option;

import static mongoose.activities.shared.logic.work.businesslogic.OptionLogic.isTouristTaxOption;

/**
 * @author Bruno Salmon
 */
public class TouristTaxRule extends BusinessRule {

    @Override
    public void apply(WorkingDocument wd) {
        if (!wd.hasAccommodation())
            wd.removeTouristTaxLine();
        else if (!wd.hasTouristTax()) {
            Option touristTaxOption = wd.getEventService().findFirstOption(o -> isTouristTaxOption(o) && (o.hasNoParent() || wd.getAccommodationLine() != null && o.getParent().getItem() == wd.getAccommodationLine().getItem()));
            if (touristTaxOption != null)
                wd.setTouristTaxLine(addNewDependentLine(wd, touristTaxOption, wd.getAccommodationLine(), 0));
        }
    }
}
