package mongoose.client.businesslogic.rules;

import mongoose.client.aggregates.event.EventAggregate;
import mongoose.shared.businessdata.time.DaysArray;
import mongoose.client.businessdata.workingdocument.WorkingDocument;
import mongoose.client.businessdata.workingdocument.WorkingDocumentLine;
import mongoose.client.businesslogic.workingdocument.BusinessType;
import mongoose.client.businesslogic.option.OptionLogic;
import mongoose.shared.entities.Option;
import webfx.platform.shared.util.collection.Collections;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author Bruno Salmon
 */
public final class BreakfastRule extends BusinessRule {

    @Override
    public void apply(WorkingDocument wd) {
        // First fast check: if there is no accommodation or no meals, there is no breakfast
        if (!wd.hasAccommodation() || !wd.hasMeals())
            wd.removeBreakfast();
        else {
            // Getting the breakfast option
            Option breakfastOption = getBreakfastOption(wd.getEventAggregate());
            if (breakfastOption != null) {
                // Fetching the existing meals and accommodation lines
                List<WorkingDocumentLine>
                        breakfastLines = new ArrayList<>(wd.getBusinessLines(BusinessType.BREAKFAST).getBusinessWorkingDocumentLines()),
                        lunchLines = wd.getBusinessLines(BusinessType.LUNCH).getBusinessWorkingDocumentLines(),
                        supperLines = wd.getBusinessLines(BusinessType.SUPPER).getBusinessWorkingDocumentLines(),
                        accommodationLines = wd.getBusinessLines(BusinessType.ACCOMMODATION).getBusinessWorkingDocumentLines();
                // Iterating over each accommodation line to add or update the associated breakfast line
                for (WorkingDocumentLine accommodationLine : accommodationLines)
                    addOrUpdateBreakfastLine(accommodationLine, breakfastLines, lunchLines, supperLines, breakfastOption);
                // All remaining (ie not updated) breakfast lines must be removed
                wd.getBusinessLines(BusinessType.BREAKFAST).removeLines(breakfastLines);
            }
        }
    }

    private static void addOrUpdateBreakfastLine(WorkingDocumentLine accommodationLine, List<WorkingDocumentLine> breakfastLines, List<WorkingDocumentLine> lunchLines, List<WorkingDocumentLine> supperLines, Option breakfastOption) {
        // First check: breakfast is not added if accommodation is on another site
        if (breakfastOption.getSite() != accommodationLine.getSite())
            return;
        // Second check: breakfast is not added if accommodation line has no day
        DaysArray accommodationDays = accommodationLine.getDaysArray().changeTimeUnit(TimeUnit.DAYS);
        if (accommodationDays.isEmpty())
            return;
        DaysArray breakfastDays = accommodationDays.shift(1); // same attendance as accommodation but +1 day
        // Third check: breakfast is not added if there is no lunch and supper over that period
        if (Collections.noneMatch(supperLines, wdl -> wdl.getDaysArray().overlaps(accommodationDays)) && Collections.noneMatch(lunchLines, wdl -> wdl.getDaysArray().overlaps(breakfastDays)))
            return;
        // All checks passed, so we need to add breakfast!
        // Trying to update an existing breakfast line (it must be the one overlapping that period if several)
        WorkingDocumentLine breakfastLine = Collections.findFirst(breakfastLines, wdl -> wdl.getOption() == breakfastOption && wdl.getDaysArray().changeTimeUnit(TimeUnit.DAYS).overlaps(breakfastDays));
        if (breakfastLine == null) // If not found, creating a new line
            addNewDependentLine(accommodationLine.getWorkingDocument(), breakfastOption, accommodationLine, 1);
        else { // If found, setting the days to apply
            breakfastLine.setDaysArray(breakfastDays);
            breakfastLines.remove(breakfastLine); // Also removing it from the list to not reuse it for a second update
        }
    }

    private static Option getBreakfastOption(EventAggregate eventAggregate) {
        Option breakfastOption = eventAggregate.getBreakfastOption();
        if (breakfastOption == null)
            eventAggregate.setBreakfastOption(breakfastOption = eventAggregate.findFirstConcreteOption(OptionLogic::isBreakfastOption));
        return breakfastOption;
    }
}
