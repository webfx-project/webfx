package mongoose.activities.shared.logic.work.businesslogic.rules;

import mongoose.activities.shared.logic.time.DaysArrayBuilder;
import mongoose.activities.shared.logic.work.WorkingDocument;
import mongoose.activities.shared.logic.work.WorkingDocumentLine;
import mongoose.activities.shared.logic.work.businesslogic.OptionLogic;
import mongoose.entities.Option;
import mongoose.services.EventService;

/**
 * @author Bruno Salmon
 */
public class DietRule extends BusinessRule {

    @Override
    public void apply(WorkingDocument wd) {
        if (!wd.hasMeals())
            wd.removeDiet();
        else {
            WorkingDocumentLine dietLine = wd.getDietLine();
            if (dietLine == null) {
                Option dietOption = getDefaultDietOption(wd.getEventService());
                if (dietOption == null)
                    return;
                wd.getWorkingDocumentLines().add(dietLine = new WorkingDocumentLine(dietOption, wd));
            }
            DaysArrayBuilder dab = new DaysArrayBuilder();
            if (wd.hasLunch())
                dab.addSeries(wd.getLunchLine().getDaysArray().toSeries(), null);
            if (wd.hasSupper())
                dab.addSeries(wd.getSupperLine().getDaysArray().toSeries(), null);
            dietLine.setDaysArray(dab.build());
        }
    }

    private static Option getDefaultDietOption(EventService eventService) {
        Option defaultDietOption = eventService.getDefaultDietOption();
        // If meals are included by default, then we return a default diet option (the first proposed one) which will be
        // automatically selected as initial choice
        if (defaultDietOption == null && OptionLogic.areMealsIncludedByDefault(eventService))
            eventService.setDefaultDietOption(defaultDietOption = eventService.findFirstConcreteOption(Option::isDiet));
        // If meals are not included by default, we don't return a default diet option so bookers will need to
        // explicitly select the diet option when ticking meals (the diet option will initially be blank)
        return defaultDietOption;
    }
}
