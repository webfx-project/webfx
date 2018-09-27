package mongooses.core.sharedends.businesslogic.rules;

import mongooses.core.shared.domainmodel.time.DaysArrayBuilder;
import mongooses.core.sharedends.businesslogic.workingdocument.WorkingDocument;
import mongooses.core.sharedends.businesslogic.workingdocument.WorkingDocumentLine;
import mongooses.core.sharedends.businesslogic.workingdocument.BusinessType;
import mongooses.core.sharedends.businesslogic.option.OptionLogic;
import mongooses.core.shared.entities.Option;
import mongooses.core.sharedends.aggregates.EventAggregate;

/**
 * @author Bruno Salmon
 */
public final class DietRule extends BusinessRule {

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
