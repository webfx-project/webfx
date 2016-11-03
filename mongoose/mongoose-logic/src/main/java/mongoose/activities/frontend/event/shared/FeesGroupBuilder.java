package mongoose.activities.frontend.event.shared;

import mongoose.activities.shared.logic.preselection.OptionsPreselection;
import mongoose.activities.shared.logic.preselection.OptionsPreselectionBuilder;
import mongoose.entities.DateInfo;
import mongoose.entities.Event;
import mongoose.entities.Label;
import mongoose.entities.Option;
import naga.commons.util.collection.Collections;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Bruno Salmon
 */
public class FeesGroupBuilder {

    private DateInfo dateInfo;
    private Event event;
    private Object id;
    private Label label;
    private Label feesBottomLabel;
    private Label feesPopupLabel;
    private boolean forceSoldout;

    private Iterable<Option> defaultOptions;
    private Iterable<Option> accommodationOptions;

    public FeesGroupBuilder setDateInfo(DateInfo dateInfo) {
        this.dateInfo = dateInfo;
        if (dateInfo != null) {
            id = dateInfo.getId();
            label = dateInfo.getLabel();
            feesBottomLabel = dateInfo.getFeesBottomLabel();
            feesPopupLabel = dateInfo.getFeesPopupLabel();
            forceSoldout = dateInfo.isForceSoldout();
        }
        return this;
    }

    public FeesGroupBuilder setEvent(Event event) {
        this.event = event;
        return this;
    }

    private Event getEvent() {
        if (event == null)
            event = dateInfo.getEvent();
        return event;
    }

    public FeesGroupBuilder setDefaultOptions(Iterable<Option> defaultOptions) {
        this.defaultOptions = defaultOptions;
        return this;
    }

    public FeesGroupBuilder setAccommodationOptions(Iterable<Option> accommodationOptions) {
        this.accommodationOptions = accommodationOptions;
        return this;
    }

    private boolean includeNoAccommodation() {
        return !getEvent().getName().contains("Overnight");
    }

    public FeesGroup build() {
        String dateTimeRange = dateInfo == null ? null : dateInfo.getDateTimeRange();
        if (dateTimeRange == null)
            dateTimeRange = getEvent().getDateTimeRange();
        List<OptionsPreselection> optionsPreselections = new ArrayList<>();
        if (accommodationOptions != null)
            for (Option accommodationOption : accommodationOptions)
                Collections.addIfNotNull(new OptionsPreselectionBuilder(dateTimeRange)
                        .addDefaultOptions(defaultOptions)
                        .addAccommodationOption(accommodationOption)
                        .build(), optionsPreselections);
        // Adding Course or No accommodation option
        if (optionsPreselections.isEmpty() || // Ex: a day course or a section with no accommodation (like Food for Thought)
                includeNoAccommodation())     // If there are accommodation options, checking we can offer no accommodation (not the case for Refresh and Revive Overnighter)
            optionsPreselections.add(new OptionsPreselectionBuilder(dateTimeRange)
                    .addDefaultOptions(defaultOptions)
                    .build());

        return new FeesGroup(id, label, feesBottomLabel, feesPopupLabel, forceSoldout, Collections.toArray(optionsPreselections, OptionsPreselection[]::new));
    }
}
