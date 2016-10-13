package mongoose.activities.shared.logic.preselection;

import mongoose.activities.shared.logic.time.DateTimeRange;
import mongoose.entities.Event;
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

    public OptionsPreselectionBuilder(Event event) {
        this(event.getDateTimeRange());
    }

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
            addOption(option);
            if (label == null)
                label = Labels.bestLabelOrName(option);
        }
        return this;
    }

    private OptionsPreselectionBuilder addOption(Option option) {
        String optionDateTimeRange = option.getDateTimeRangeOrParent();
        DateTimeRange finalDateTimeRange = DateTimeRange.parse(dateTimeRange);
        if (optionDateTimeRange != null)
            finalDateTimeRange = finalDateTimeRange.intersect(DateTimeRange.parse(optionDateTimeRange));
        return addOptionPreselection(new OptionPreselection(option, finalDateTimeRange, option.getTimeRangeOrParent()));
    }

    private OptionsPreselectionBuilder addOptionPreselection(OptionPreselection optionPreselection) {
        optionPreselections.add(optionPreselection);
        return this;
    }

    public OptionsPreselection build() {
        return new OptionsPreselection(label, optionPreselections);
    }

}
