package mongooses.core.sharedends.businesslogic.rules;

import mongooses.core.sharedends.businesslogic.workingdocument.WorkingDocument;
import mongooses.core.shared.entities.Option;

import static mongooses.core.sharedends.businesslogic.option.OptionLogic.isTouristTaxOption;

/**
 * @author Bruno Salmon
 */
public final class TouristTaxRule extends BusinessRule {

    @Override
    public void apply(WorkingDocument wd) {
        if (!wd.hasAccommodation())
            wd.removeTouristTax();
        else if (!wd.hasTouristTax()) {
            Option touristTaxOption = wd.getEventAggregate().findFirstOption(o -> isTouristTaxOption(o) && (o.hasNoParent() || wd.getAccommodationLine() != null && o.getParent().getItem() == wd.getAccommodationLine().getItem()));
            if (touristTaxOption != null)
                addNewDependentLine(wd, touristTaxOption, wd.getAccommodationLine(), 0);
        }
    }
}
