package mongoose.client.businesslogic.rules;

import mongoose.shared.businessdata.time.DaysArray;
import mongoose.client.businessdata.workingdocument.WorkingDocument;
import mongoose.client.businessdata.workingdocument.WorkingDocumentLine;
import mongoose.client.businesslogic.workingdocument.BusinessType;
import mongoose.client.businesslogic.option.OptionLogic;
import mongoose.shared.entities.Option;
import mongoose.shared.entities.Site;
import webfx.platform.shared.util.collection.Collections;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author Bruno Salmon
 */
public final class HotelShuttleRule extends BusinessRule {

    @Override
    public void apply(WorkingDocument wd) {
        // First fast check: if there is no accommodation, there is no hotel shuttle
        if (!wd.hasAccommodation())
            wd.removeHotelShuttle();
        else {
            List<Option> selectedHotelShuttleOptions = wd.getIsOptionSelectedInOptionTreePredicate() == null ? null : // null if the working document is not from the options activity
                    wd.getEventAggregate().selectOptions(o -> OptionLogic.isHotelShuttleOption(o) && wd.getIsOptionSelectedInOptionTreePredicate().test(o));
            // Fetching the existing hotel shuttle
            List<WorkingDocumentLine> hotelShuttleLines = wd.getBusinessLines(BusinessType.HOTEL_SHUTTLE).getBusinessWorkingDocumentLines();
            // Adding or updating these lines (unless there are no options nor lines)
            if (!Collections.isEmpty(selectedHotelShuttleOptions) || !hotelShuttleLines.isEmpty()) {
                hotelShuttleLines = new ArrayList<>(hotelShuttleLines); // Duplicating the list because updated shuttle lines will be removed from that list
                // Iterating over each accommodation line to add or update the associated hotel shuttle lines
                for (WorkingDocumentLine accommodationLine : wd.getBusinessLines(BusinessType.ACCOMMODATION).getBusinessWorkingDocumentLines())
                    addOrUpdateHotelShuttleLines(accommodationLine, hotelShuttleLines, selectedHotelShuttleOptions);
            }
            // All remaining (ie not updated) hotel shuttle lines must be removed
            wd.getBusinessLines(BusinessType.HOTEL_SHUTTLE).removeLines(hotelShuttleLines);
        }
    }

    private static void addOrUpdateHotelShuttleLines(WorkingDocumentLine accommodationLine, List<WorkingDocumentLine> hotelShuttleLines, List<Option> selectedHotelShuttleOptions) {
        // First check: accommodation line must be a hotel
        Site hotel = accommodationLine.getSite();
        if (!OptionLogic.isHotel(hotel))
            return;
        // Second check: hotel shuttle is not added if accommodation line has no day
        DaysArray accommodationDays = accommodationLine.getDaysArray().changeTimeUnit(TimeUnit.DAYS);
        if (accommodationDays.isEmpty())
            return;
        // 2 iterations loop (first for inward shuttle, second for return shuttle)
        for (long shiftDays = 0; shiftDays <= 1; shiftDays++) {
            boolean inward = shiftDays == 0;
            // Second check: hotel shuttle is not added if there is no such inward/return selected option for this hotel
            Option selectedHotelShuttleOption;
            if (selectedHotelShuttleOptions != null) {
                selectedHotelShuttleOption = Collections.findFirst(selectedHotelShuttleOptions, o -> (inward ? o.getArrivalSite() : o.getSite()) == hotel);
                if (selectedHotelShuttleOption == null)
                    return;
            } else
                selectedHotelShuttleOption = null;
            DaysArray hotelShuttleDays = accommodationDays.shift(shiftDays); // same attendance as accommodation for inward and +1 day for return shuttle
            // All checks passed, so we need to add hotel shuttle!
            // Trying to update an existing hotel shuttle line (it must be the one overlapping that period if several)
            WorkingDocumentLine hotelShuttleLine = Collections.findFirst(hotelShuttleLines, wdl ->
                    (selectedHotelShuttleOption != null ? wdl.getOption() == selectedHotelShuttleOption : (inward ? wdl.getArrivalSite() : wdl.getSite()) == hotel) &&
                    wdl.getDaysArray().changeTimeUnit(TimeUnit.DAYS).overlaps(hotelShuttleDays));
            if (hotelShuttleLine != null) { // If found, setting the days to apply
                hotelShuttleLine.setDaysArray(hotelShuttleDays);
                hotelShuttleLines.remove(hotelShuttleLine); // Also removing it from the list to not reuse it for a second update
            } else if (selectedHotelShuttleOption != null)// If not found, creating a new line
                addNewDependentLine(accommodationLine.getWorkingDocument(), selectedHotelShuttleOption, accommodationLine, shiftDays);
        }
    }

}
