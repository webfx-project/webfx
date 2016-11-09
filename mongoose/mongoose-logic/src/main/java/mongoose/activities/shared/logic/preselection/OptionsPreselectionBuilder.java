package mongoose.activities.shared.logic.preselection;

import mongoose.activities.shared.logic.time.DateTimeRange;
import mongoose.activities.shared.logic.time.DayTimeRange;
import mongoose.entities.Label;
import mongoose.entities.Option;
import mongoose.services.EventService;
import mongoose.util.Labels;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Bruno Salmon
 */
public class OptionsPreselectionBuilder {

    private final EventService eventService;
    private final DateTimeRange dateTimeRange;
    private final List<OptionPreselection> optionPreselections = new ArrayList<>();
    private Label label;
    private boolean hasAccommodation;
    private boolean nightIsCovered;

    public OptionsPreselectionBuilder(EventService eventService, DateTimeRange dateTimeRange) {
        this.eventService = eventService;
        this.dateTimeRange = dateTimeRange;
    }

    public OptionsPreselectionBuilder addDefaultOptions(Iterable<Option> options) {
        for (Option option : options)
            addOption(option);
        return this;
    }

    public OptionsPreselectionBuilder addAccommodationOption(Option option) {
        if (option != null) {
            hasAccommodation = true;
            if (addOption(option))
                nightIsCovered = true;
            label = Labels.bestLabelOrName(option);
        }
        return this;
    }

    private boolean addOption(Option option) {
        DateTimeRange optionDateTimeRange = option.getParsedDateTimeRangeOrParent();
        DateTimeRange finalDateTimeRange = dateTimeRange;
        if (optionDateTimeRange != null)
            finalDateTimeRange = finalDateTimeRange.intersect(optionDateTimeRange);
        DayTimeRange dayTimeRange = option.getParsedTimeRangeOrParent();
        if (dayTimeRange != null)
            finalDateTimeRange = finalDateTimeRange.intersect(dayTimeRange);
        if (finalDateTimeRange.isEmpty())
            return false;
        optionPreselections.add(new OptionPreselection(option, finalDateTimeRange, dayTimeRange));
        return true;
    }

    public OptionsPreselection build() {
        return hasAccommodation && !nightIsCovered ? null : new OptionsPreselection(eventService, label, optionPreselections);
    }

}
