package mongoose.activities.bothends.logic.work.business.logic;

import mongoose.activities.bothends.logic.time.DayTimeRange;
import mongoose.activities.bothends.logic.time.TimeInterval;
import mongoose.entities.Event;
import mongoose.entities.Option;
import mongoose.entities.Site;
import mongoose.services.EventService;
import naga.util.Numbers;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author Bruno Salmon
 */
public class OptionLogic {

    public static boolean isOptionDisplayableOnCalendar(Option option, boolean isMax) {
        return option != null && ((option.isTeaching() || !isMax && (option.isMeals() || option.isAccommodation() || option.isTransport())) && option.getParsedTimeRangeOrParent() != null);
    }

    private static boolean isOptionManagedByBusinessRules(Option o) {
        return isBreakfastOption(o)
                || o.isDiet()
                || isTouristTaxOption(o)
                || o.isTranslation();
    }

    static List<Option> selectDefaultOptions(EventService eventService) {
        return eventService.selectOptions(o -> isOptionIncludedByDefault(o, eventService));
    }

    public static boolean areMealsIncludedByDefault(EventService eventService) {
        // Answer: yes except for day courses, public talks and International Festivals
        Event event = eventService.getEvent();
        String eventName = event.getName();
        return !eventName.contains("Day Course")
                && !eventName.contains("Public Talk")
                && Numbers.toInteger(event.getOrganizationId().getPrimaryKey()) != 1;
    }

    private static boolean isOptionIncludedByDefault(Option o, EventService eventService) {
        return (o.isConcrete() || o.hasItem() && o.hasTimeRange()/* Ex: Prayers -> to include in the working document so it is displayed in the calendar*/ )
                && !isOptionManagedByBusinessRules(o)
                && (o.isTeaching() || (o.isMeals() ? areMealsIncludedByDefault(eventService) : o.isObligatory() && (o.hasNoParent() || isOptionIncludedByDefault(o.getParent(), eventService))))
                ;
    }

    public static boolean isBreakfastOption(Option option) {
        return isMealsOptionInDayTimeRange(option, 0, 10 * 60);
    }

    public static boolean isLunchOption(Option option) {
        return isMealsOptionInDayTimeRange(option, 10 * 60, 15 * 60);
    }

    public static boolean isSupperOption(Option option) {
        return isMealsOptionInDayTimeRange(option,15 * 60, 24 * 60);
    }

    private static boolean isMealsOptionInDayTimeRange(Option option, long startMinutes, long endMinutes) {
        return option.isMeals() && isOptionInDayTimeRange(option, startMinutes, endMinutes);
    }

    private static boolean isOptionInDayTimeRange(Option option, long startMinutes, long endMinutes) {
        DayTimeRange dayTimeRange = option.getParsedTimeRangeOrParent();
        if (dayTimeRange == null)
            return false;
        TimeInterval dayTimeInterval = dayTimeRange.getDayTimeInterval(0, TimeUnit.DAYS);
        return dayTimeInterval.getIncludedStart() >= startMinutes && dayTimeInterval.getExcludedEnd() < endMinutes;
    }

    public static boolean isTouristTaxOption(Option option) {
        return option.isTax(); // The only tax for now is the tourist tax
    }

    public static boolean isHotelShuttleOption(Option option) {
        return option.isTransport() && (isHotel(option.getSite()) || isHotel(option.getArrivalSite()));
    }

    public static boolean isHotel(Site site) {
        return site != null && site.isAccommodation() && !site.isMain(); // Excluding the main site
    }

    public static boolean isAirportShuttleOption(Option option) {
        return option.isTransport() && (isAirport(option.getSite()) || isAirport(option.getArrivalSite()));
    }

    public static boolean isAirport(Site site) {
        return site != null && site.isTransport();
    }

    public static boolean isOptionAttendanceVariable(Option option) {
        return !isAirportShuttleOption(option);
    }
}
