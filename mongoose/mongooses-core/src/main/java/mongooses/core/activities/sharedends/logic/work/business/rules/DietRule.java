package mongooses.core.activities.sharedends.logic.work.business.rules;

import mongooses.core.activities.sharedends.logic.time.DaysArrayBuilder;
import mongooses.core.activities.sharedends.logic.work.WorkingDocument;
import mongooses.core.activities.sharedends.logic.work.WorkingDocumentLine;
import mongooses.core.activities.sharedends.logic.work.business.BusinessType;
import mongooses.core.activities.sharedends.logic.work.business.logic.OptionLogic;
import mongooses.core.entities.Option;
import mongooses.core.aggregates.EventAggregate;

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
                Option dietOption = getDefaultDietOption(wd.getEventAggregate());
                if (dietOption == null)
                    return;
                wd.getWorkingDocumentLines().add(dietLine = new WorkingDocumentLine(dietOption, wd, null));
            }
            DaysArrayBuilder dab = new DaysArrayBuilder();
            for (WorkingDocumentLine mealsLine : wd.getBusinessLines(BusinessType.LUNCH).getBusinessWorkingDocumentLines())
                dab.addDaysArray(mealsLine.getDaysArray(), null);
            for (WorkingDocumentLine mealsLine : wd.getBusinessLines(BusinessType.SUPPER).getBusinessWorkingDocumentLines())
                dab.addDaysArray(mealsLine.getDaysArray(), null);
            dietLine.setDaysArray(dab.build());
        }
    }

    private static Option getDefaultDietOption(EventAggregate eventAggregate) {
        Option defaultDietOption = eventAggregate.getDefaultDietOption();
        // If meals are included by default, then we return a default diet option (the first proposed one) which will be
        // automatically selected as initial choice
        if (defaultDietOption == null && OptionLogic.areMealsIncludedByDefault(eventAggregate))
            eventAggregate.setDefaultDietOption(defaultDietOption = eventAggregate.findFirstConcreteOption(Option::isDiet));
        // If meals are not included by default, we don't return a default diet option so bookers will need to
        // explicitly select the diet option when ticking meals (the diet option will initially be blank)
        return defaultDietOption;
    }
}
