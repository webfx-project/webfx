package mongoose.activities.shared.logic.work.business.rules;

import mongoose.activities.shared.book.event.options.OptionTree;
import mongoose.activities.shared.logic.time.DaysArray;
import mongoose.activities.shared.logic.work.WorkingDocument;
import mongoose.activities.shared.logic.work.WorkingDocumentLine;
import mongoose.activities.shared.logic.work.business.BusinessType;
import mongoose.activities.shared.logic.work.business.logic.OptionLogic;
import mongoose.entities.Option;
import mongoose.entities.Site;
import naga.util.collection.Collections;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author Bruno Salmon
 */
public class HotelShuttleRule extends BusinessRule {

    @Override
    public void apply(WorkingDocument wd) {
        // First fast check: if there is no accommodation, there is no hotel shuttle
        if (!wd.hasAccommodation())
            wd.removeHotelShuttle();
        else {
            OptionTree optionTree = wd.getOptionTree();
            List<Option> hotelShuttleOptions = wd.getEventService().selectOptions(o -> OptionLogic.isHotelShuttleOption(o) && (optionTree == null || optionTree.isOptionSelected(o)));
            if (!Collections.isEmpty(hotelShuttleOptions)) {
                // Fetching the existing hotel shuttle and accommodation lines
                List<WorkingDocumentLine>
                        hotelShuttleLines = new ArrayList<>(wd.getBusinessLines(BusinessType.HOTEL_SHUTTLE).getBusinessWorkingDocumentLines()),
                        accommodationLines = wd.getBusinessLines(BusinessType.ACCOMMODATION).getBusinessWorkingDocumentLines();
                // Iterating over each accommodation line to add or update the associated hotel shuttle lines
                for (WorkingDocumentLine accommodationLine : accommodationLines)
                    addOrUpdateHotelShuttleLines(accommodationLine, hotelShuttleLines, hotelShuttleOptions);
                // All remaining (ie not updated) hotel shuttle lines must be removed
                wd.getBusinessLines(BusinessType.HOTEL_SHUTTLE).removeLines(hotelShuttleLines);
            }
        }
    }

    private static void addOrUpdateHotelShuttleLines(WorkingDocumentLine accommodationLine, List<WorkingDocumentLine> hotelShuttleLines, List<Option> hotelShuttleOptions) {
        addOrUpdateInwardHotelShuttleLine(accommodationLine, hotelShuttleLines, hotelShuttleOptions);
        addOrUpdateReturnHotelShuttleLine(accommodationLine, hotelShuttleLines, hotelShuttleOptions);
    }

    private static void addOrUpdateInwardHotelShuttleLine(WorkingDocumentLine accommodationLine, List<WorkingDocumentLine> hotelShuttleLines, List<Option> hotelShuttleOptions) {
        addOrUpdateHotelShuttleLine(accommodationLine, hotelShuttleLines, hotelShuttleOptions, true);
    }

    private static void addOrUpdateReturnHotelShuttleLine(WorkingDocumentLine accommodationLine, List<WorkingDocumentLine> hotelShuttleLines, List<Option> hotelShuttleOptions) {
        addOrUpdateHotelShuttleLine(accommodationLine, hotelShuttleLines, hotelShuttleOptions, false);
    }

    private static void addOrUpdateHotelShuttleLine(WorkingDocumentLine accommodationLine, List<WorkingDocumentLine> hotelShuttleLines, List<Option> transportOptions, boolean inward) {
        Site hotel = accommodationLine.getSite();
        if (!OptionLogic.isHotel(hotel))
            return;
        // First check: hotel shuttle is not added if accommodation line has no day
        DaysArray accommodationDays = accommodationLine.getDaysArray().changeTimeUnit(TimeUnit.DAYS);
        if (accommodationDays.isEmpty())
            return;
        // Second check: hotel shuttle is not added if there is no such inward/return option for this hotel
        Option hotelShuttleOption = Collections.findFirst(transportOptions, o -> (inward ? o.getArrivalSite() : o.getSite()) == hotel);
        if (hotelShuttleOption == null)
            return;
        long shiftDays = inward ? 0 : 1; // same attendance as accommodation but +1 day for return shuttle
        DaysArray hotelShuttleDays = accommodationDays.shift(shiftDays);
        // All checks passed, so we need to add hotel shuttle!
        // Trying to update an existing hotel shuttle line (it must be the one overlapping that period if several)
        WorkingDocumentLine hotelShuttleLine = Collections.findFirst(hotelShuttleLines, wdl -> wdl.getOption() == hotelShuttleOption && wdl.getDaysArray().changeTimeUnit(TimeUnit.DAYS).overlaps(hotelShuttleDays));
        if (hotelShuttleLine == null) // If not found, creating a new line
            addNewDependentLine(accommodationLine.getWorkingDocument(), hotelShuttleOption, accommodationLine, shiftDays);
        else { // If found, setting the days to apply
            hotelShuttleLine.setDaysArray(hotelShuttleDays);
            hotelShuttleLines.remove(hotelShuttleLine); // Also removing it from the list to not reuse it for a second update
        }
    }

}
