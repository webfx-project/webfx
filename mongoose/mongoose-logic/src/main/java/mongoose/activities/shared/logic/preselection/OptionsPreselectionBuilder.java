package mongoose.activities.shared.logic.preselection;

import mongoose.activities.shared.logic.time.DateTimeRange;
import mongoose.activities.shared.logic.time.DayTimeRange;
import mongoose.entities.Label;
import mongoose.entities.Option;
import mongoose.util.Labels;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Bruno Salmon
 */
public class OptionsPreselectionBuilder {

    private Label label;
    private final List<OptionPreselection> optionPreselections = new ArrayList<>();
    private final String dateTimeRange;
    private boolean hasAccommodation;
    private boolean nightIsCovered;

    public OptionsPreselectionBuilder(String dateTimeRange) {
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
        String optionDateTimeRange = option.getDateTimeRangeOrParent();
        DateTimeRange finalDateTimeRange = DateTimeRange.parse(dateTimeRange);
        if (optionDateTimeRange != null)
            finalDateTimeRange = finalDateTimeRange.intersect(DateTimeRange.parse(optionDateTimeRange));
        DayTimeRange dayTimeRange = DayTimeRange.parse(option.getTimeRangeOrParent());
        if (dayTimeRange != null)
            finalDateTimeRange = finalDateTimeRange.intersect(dayTimeRange);
        if (finalDateTimeRange.isEmpty())
            return false;
        optionPreselections.add(new OptionPreselection(option, finalDateTimeRange, dayTimeRange));
        return true;
    }

    public OptionsPreselection build() {
        return hasAccommodation && !nightIsCovered ? null : new OptionsPreselection(label, optionPreselections);
    }

}
