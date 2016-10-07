package mongoose.activities.shared.logic.preselection;

import mongoose.entities.Event;
import mongoose.entities.Label;
import mongoose.entities.Option;
import naga.commons.util.Objects;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Bruno Salmon
 */
public class OptionsPreselectionBuilder {

    private Label label;
    private final List<OptionPreselection> optionPreselections = new ArrayList<>();
    private final String eventDateTimeRange;

    public OptionsPreselectionBuilder(Event event) {
        this(event.getDateTimeRange());
    }

    public OptionsPreselectionBuilder(String eventDateTimeRange) {
        this.eventDateTimeRange = eventDateTimeRange;
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
                label = option.bestLabel();
        }
        return this;
    }

    private OptionsPreselectionBuilder addOption(Option option) {
        return addOptionPreselection(new OptionPreselection(option, Objects.coalesce(option.getDateTimeRangeOrParent(), eventDateTimeRange), option.getTimeRangeOrParent()));
    }

    private OptionsPreselectionBuilder addOptionPreselection(OptionPreselection optionPreselection) {
        optionPreselections.add(optionPreselection);
        return this;
    }

    public OptionsPreselection build() {
        return new OptionsPreselection(label, optionPreselections);
    }

}
