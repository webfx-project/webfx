package mongoose.activities.shared.logic.work.businesslogic.rules;

import mongoose.activities.shared.logic.work.WorkingDocument;
import mongoose.activities.shared.logic.work.WorkingDocumentLine;
import mongoose.entities.Option;
import mongoose.entities.Site;
import naga.util.collection.Collections;
import naga.util.function.Predicate;

/**
 * @author Bruno Salmon
 */
public class HotelShuttleRules extends BusinessRule {

    @Override
    public void apply(WorkingDocument wd) {
        Site hotel = wd.hasAccommodation() ? wd.getAccommodationLine().getSite() : null;
        removeLines(wd, wdl -> isAHotelShuttleLineButOtherThanForThisHotel(wdl, hotel));
    }

    private static boolean isAHotelShuttleLineButOtherThanForThisHotel(WorkingDocumentLine wdl, Site hotel) {
        return wdl.isTransport() && (isAHotelButOtherThan(wdl.getSite(), hotel) || isAHotelButOtherThan(wdl.getArrivalSite(), hotel));
    }

    private static boolean isAHotelButOtherThan(Site site, Site hotel) {
        return site != null && site.isAccommodation() && site != hotel;
    }

    private static void removeLines(WorkingDocument wd, Predicate<WorkingDocumentLine> predicate) {
        Collections.removeIf(wd.getWorkingDocumentLines(), predicate);
    }

    public static boolean isMorningHotelShuttleOption(Option option, WorkingDocument wd) {
        return option.isTransport() && option.getSite() == wd.getAccommodationLine(); // && isOptionInDayTimeRange(option, 0, 12 * 60);
    }

    public static boolean isEveningHotelShuttleOption(Option option, WorkingDocument wd) {
        return option.isTransport() && option.getArrivalSite() == wd.getAccommodationLine(); // && isOptionInDayTimeRange(option, 0, 12 * 60);
    }

}
