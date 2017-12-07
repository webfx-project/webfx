package mongoose.activities.shared.logic.work.rules;

import mongoose.activities.shared.book.event.shared.FeesGroup;
import mongoose.activities.shared.book.event.shared.FeesGroupBuilder;
import mongoose.entities.DateInfo;
import mongoose.entities.Option;
import mongoose.entities.Site;
import mongoose.services.EventService;
import mongoose.util.Labels;
import naga.framework.orm.entity.EntityList;
import naga.util.collection.Collections;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Bruno Salmon
 */
public class FeesGroupRules {

    public static FeesGroup[] createFeesGroups(EventService eventService) {
        List<FeesGroup> feesGroups = new ArrayList<>();
        EntityList<DateInfo> dateInfos = eventService.getEventDateInfos();
        List<Option> defaultOptions = OptionRules.selectDefaultOptions(eventService);
        List<Option> accommodationOptions = eventService.selectOptions(o -> o.isConcrete() && o.isAccommodation());
        if (!dateInfos.isEmpty())
            for (DateInfo dateInfo : dateInfos)
                populateFeesGroups(eventService, dateInfo, defaultOptions, accommodationOptions, feesGroups);
        else if (eventService.getEvent() != null) // May happen if event is empty (ie has no option)
            populateFeesGroups(eventService, null, defaultOptions, accommodationOptions, feesGroups);
        return Collections.toArray(feesGroups, FeesGroup[]::new);
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
}
