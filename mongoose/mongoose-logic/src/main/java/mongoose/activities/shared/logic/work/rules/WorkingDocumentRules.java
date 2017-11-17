package mongoose.activities.shared.logic.work.rules;

import mongoose.activities.shared.book.event.shared.FeesGroup;
import mongoose.activities.shared.book.event.shared.FeesGroupBuilder;
import mongoose.activities.shared.logic.time.DayTimeRange;
import mongoose.activities.shared.logic.time.DaysArrayBuilder;
import mongoose.activities.shared.logic.time.TimeInterval;
import mongoose.activities.shared.logic.work.WorkingDocument;
import mongoose.activities.shared.logic.work.WorkingDocumentLine;
import mongoose.entities.*;
import mongoose.services.EventService;
import mongoose.util.Labels;
import naga.framework.orm.entity.EntityList;
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
        applyBreakfastRule(workingDocument);
        applyDietRule(workingDocument);
        applyTouristTaxRule(workingDocument);
        applyTranslationRule(workingDocument);
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

    private static void applyBreakfastRule(WorkingDocument wd) {
        if (!wd.hasAccommodation() || !wd.hasMeals())
            wd.removeBreakfastLine();
        else if (!wd.hasBreakfast()) {
            Option breakfastOption = getBreakfastOption(wd.getEventService());
            if (breakfastOption != null)
                wd.setBreakfastLine(addNewDependentLine(wd, breakfastOption, wd.getAccommodationLine(), 1));
        }
    }

    private static void applyDietRule(WorkingDocument wd) {
        if (!wd.hasMeals())
            wd.removeDietLine();
        else {
            WorkingDocumentLine dietLine = wd.getDietLine();
            if (dietLine == null) {
                Option dietOption = getDefaultDietOption(wd.getEventService());
                if (dietOption == null)
                    return;
                wd.setDietLine(dietLine = new WorkingDocumentLine(dietOption, wd));
                wd.getWorkingDocumentLines().add(dietLine);
            }
            DaysArrayBuilder dab = new DaysArrayBuilder();
            if (wd.hasLunch())
                dab.addSeries(wd.getLunchLine().getDaysArray().toSeries(), null);
            if (wd.hasSupper())
                dab.addSeries(wd.getSupperLine().getDaysArray().toSeries(), null);
            dietLine.setDaysArray(dab.build());
        }
    }

    private static void applyTouristTaxRule(WorkingDocument wd) {
        if (!wd.hasAccommodation())
            wd.removeTouristTaxLine();
        else if (!wd.hasTouristTax()) {
            Option touristTaxOption = wd.getEventService().findFirstOption(o -> isTouristTaxOption(o) && (o.getParent() == null || wd.getAccommodationLine() != null && o.getParent().getItem() == wd.getAccommodationLine().getItem()));
            if (touristTaxOption != null)
                wd.setTouristTaxLine(addNewDependentLine(wd, touristTaxOption, wd.getAccommodationLine(), 0));
        }
    }

    private static void applyTranslationRule(WorkingDocument wd) {
        if (!wd.hasTeaching())
            wd.removeTranslationLine();
        else if (wd.hasTranslation())
            applySameAttendances(wd.getTranslationLine(), wd.getTeachingLine(), 0);
    }

    private static WorkingDocumentLine addNewDependentLine(WorkingDocument wd, Option dependentOption, WorkingDocumentLine masterLine, long shiftDays) {
        WorkingDocumentLine dependantLine = new WorkingDocumentLine(dependentOption, wd);
        wd.getWorkingDocumentLines().add(dependantLine);
        applySameAttendances(dependantLine, masterLine, shiftDays);
        return dependantLine;
    }

    private static void applySameAttendances(WorkingDocumentLine dependentLine, WorkingDocumentLine masterLine, long shiftDays) {
        dependentLine.setDaysArray(masterLine.getDaysArray().shift(shiftDays));
    }

    private static List<Option> selectDefaultOptions(EventService eventService) {
        return eventService.selectOptions(o -> isOptionIncludedByDefault(o, eventService));
    }

    private static boolean areMealsIncludedByDefault(EventService eventService) {
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
                && (o.isTeaching() || (o.isMeals() ? areMealsIncludedByDefault(eventService) : o.isObligatory()))
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
        if (!option.isMeals())
            return false;
        DayTimeRange dayTimeRange = option.getParsedTimeRangeOrParent();
        if (dayTimeRange == null)
            return false;
        TimeInterval dayTimeInterval = dayTimeRange.getDayTimeInterval(0, TimeUnit.DAYS);
        return dayTimeInterval.getIncludedStart() >= startMinutes && dayTimeInterval.getExcludedEnd() < endMinutes;
    }

    public static boolean isTouristTaxOption(Option option) {
        return option.isTax(); // The only tax for now is the tourist tax
    }

    private static Option getBreakfastOption(EventService eventService) {
        Option breakfastOption = eventService.getBreakfastOption();
        if (breakfastOption == null)
            eventService.setBreakfastOption(breakfastOption = eventService.findFirstConcreteOption(WorkingDocumentRules::isBreakfastOption));
        return breakfastOption;
    }

    private static Option getDefaultDietOption(EventService eventService) {
        Option defaultDietOption = eventService.getDefaultDietOption();
        // If meals are included by default, then we return a default diet option (the first proposed one) which will be
        // automatically selected as initial choice
        if (defaultDietOption == null && areMealsIncludedByDefault(eventService))
            eventService.setDefaultDietOption(defaultDietOption = eventService.findFirstConcreteOption(Option::isDiet));
        // If meals are not included by default, we don't return a default diet option so bookers will need to
        // explicitly select the diet option when ticking meals (the diet option will initially be blank)
        return defaultDietOption;
    }
}
