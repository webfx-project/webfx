package mongoose.activities.shared.logic.work.rules;

import mongoose.activities.shared.book.event.shared.FeesGroup;
import mongoose.activities.shared.book.event.shared.FeesGroupBuilder;
import mongoose.activities.shared.logic.time.DayTimeRange;
import mongoose.activities.shared.logic.time.TimeInterval;
import mongoose.activities.shared.logic.work.WorkingDocument;
import mongoose.entities.DateInfo;
import mongoose.entities.Event;
import mongoose.entities.Option;
import mongoose.entities.Site;
import mongoose.services.EventService;
import mongoose.util.Labels;
import naga.framework.orm.entity.EntityList;
import naga.util.Arrays;
import naga.util.Numbers;
import naga.util.collection.Collections;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author Bruno Salmon
 */
public class WorkingDocumentRules {

    private final static WorkingDocumentRule[] BUSINESS_RULES = {
            new BreakfastRule(),
            new DietRule(),
            new TouristTaxRule(),
            new TranslationRule(),
            new HotelShuttleRules()
    };

    // External entry points

    public static FeesGroup[] createFeesGroups(EventService eventService) {
        List<FeesGroup> feesGroups = new ArrayList<>();
        EntityList<DateInfo> dateInfos = eventService.getEventDateInfos();
        List<Option> defaultOptions = selectDefaultOptions(eventService);
        List<Option> accommodationOptions = eventService.selectOptions(o -> o.isConcrete() && o.isAccommodation());
        if (!dateInfos.isEmpty())
            for (DateInfo dateInfo : dateInfos)
                populateFeesGroups(eventService, dateInfo, defaultOptions, accommodationOptions, feesGroups);
        else if (eventService.getEvent() != null) // May happen if event is empty (ie has no option)
            populateFeesGroups(eventService, null, defaultOptions, accommodationOptions, feesGroups);
        return Collections.toArray(feesGroups, FeesGroup[]::new);
    }

    public static void applyBusinessRules(WorkingDocument workingDocument) {
        Arrays.forEach(BUSINESS_RULES, rule -> rule.apply(workingDocument));
    }


    public static boolean isOptionDisplayableOnCalendar(Option option, boolean isMax) {
        return option != null && ((option.isTeaching() || !isMax && (option.isMeals() || option.isAccommodation() || option.isTransport())) && option.getParsedTimeRangeOrParent() != null);
    }

    // Private implementation

    private static boolean isOptionManagedByBusinessRules(Option o) {
        return isBreakfastOption(o)
                || o.isDiet()
                || isTouristTaxOption(o)
                || o.isTranslation();
    }

    private static void populateFeesGroups(EventService eventService, DateInfo dateInfo, List<Option> defaultOptions, List<Option> accommodationOptions, List<FeesGroup> feesGroups) {
        Map<Site, List<Option>> accommodationOptionsBySite = new LinkedHashMap<>(); // accommodationOptions.stream().collect(Collectors.groupingBy(Option::getSite));
        Collections.forEach(accommodationOptions, o -> accommodationOptionsBySite.computeIfAbsent(o.getSite(), k -> new ArrayList<>()).add(o));
        boolean multiAccommodationSites = accommodationOptionsBySite.size() > 1;
        if (multiAccommodationSites)
            feesGroups.add(createFeesGroup(eventService, null, "NoAccommodation", dateInfo, defaultOptions, java.util.Collections.emptyList(), true));
        boolean addNoAccommodationOption = !multiAccommodationSites && !eventService.getEvent().getName().contains("Overnight");
        for (Map.Entry<Site, List<Option>> entry : accommodationOptionsBySite.entrySet())
            feesGroups.add(createFeesGroup(eventService, entry.getKey(), null, dateInfo, defaultOptions, entry.getValue(), addNoAccommodationOption));
    }

    private static FeesGroup createFeesGroup(EventService eventService, Object label, String i18nKey, DateInfo dateInfo, List<Option> defaultOptions, List<Option> accommodationOptions, boolean addNoAccommodationOption) {
        return new FeesGroupBuilder(eventService)
                .setLabel(Labels.bestLabelOrName(label))
                .setI18nKey(i18nKey)
                .setDateInfo(dateInfo)
                .setDefaultOptions(defaultOptions)
                .setAccommodationOptions(accommodationOptions)
                .setAddNoAccommodationOption(addNoAccommodationOption)
                .build();
    }

    private static List<Option> selectDefaultOptions(EventService eventService) {
        return eventService.selectOptions(o -> isOptionIncludedByDefault(o, eventService));
    }

    static boolean areMealsIncludedByDefault(EventService eventService) {
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
}
