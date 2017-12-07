package mongoose.activities.shared.logic.work.rules;

import mongoose.activities.shared.logic.work.WorkingDocument;
import mongoose.entities.Option;
import mongoose.services.EventService;

/**
 * @author Bruno Salmon
 */
class BreakfastRule extends WorkingDocumentRule {

    @Override
    void apply(WorkingDocument wd) {
        if (!wd.hasAccommodation() || !wd.hasMeals())
            wd.removeBreakfastLine();
        else if (!wd.hasBreakfast()) {
            Option breakfastOption = getBreakfastOption(wd.getEventService());
            // Breakfast added only if it is on same site as accommodation
            if (breakfastOption != null && breakfastOption.getSite() == wd.getAccommodationLine().getSite())
                wd.setBreakfastLine(addNewDependentLine(wd, breakfastOption, wd.getAccommodationLine(), 1));
        }
    }

    private static Option getBreakfastOption(EventService eventService) {
        Option breakfastOption = eventService.getBreakfastOption();
        if (breakfastOption == null)
            eventService.setBreakfastOption(breakfastOption = eventService.findFirstConcreteOption(WorkingDocumentRules::isBreakfastOption));
        return breakfastOption;
    }

}
