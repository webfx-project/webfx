package mongoose.activities.frontend.event.fees;

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
class FeesGroupBuilder {

    private DateInfo dateInfo;
    private Event event;
    private Object id;
    private Label label;
    private Label feesBottomLabel;
    private Label feesPopupLabel;
    private boolean forceSoldout;

    private Iterable<Option> defaultOptions;
    private Iterable<Option> accommodationOptions;

    FeesGroupBuilder setDateInfo(DateInfo dateInfo) {
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

    FeesGroupBuilder setEvent(Event event) {
        this.event = event;
        return this;
    }

    private Event getEvent() {
        if (event == null)
            event = dateInfo.getEvent();
        return event;
    }

    FeesGroupBuilder setDefaultOptions(Iterable<Option> defaultOptions) {
        this.defaultOptions = defaultOptions;
        return this;
    }

    public FeesGroupBuilder setAccommodationOptions(Iterable<Option> accommodationOptions) {
        this.accommodationOptions = accommodationOptions;
        return this;
    }

    FeesGroup build() {
        List<OptionsPreselection> optionsPreselections = new ArrayList<>();
        if (accommodationOptions != null)
            for (Option accommodationOption : accommodationOptions)
                optionsPreselections.add(new OptionsPreselectionBuilder(getEvent())
                        .addDefaultOptions(defaultOptions)
                        .addAccommodationOption(accommodationOption)
                        .build());
        // No accommodation
        optionsPreselections.add(new OptionsPreselectionBuilder(getEvent())
                .addDefaultOptions(defaultOptions)
                .build());

        return new FeesGroup(id, label, feesBottomLabel, feesPopupLabel, forceSoldout, Collections.toArray(optionsPreselections, OptionsPreselection[]::new));
    }
}
