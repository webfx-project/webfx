package mongoose.activities.frontend.event.fees;

import mongoose.activities.shared.logic.preselection.OptionsPreselectionBuilder;
import mongoose.entities.DateInfo;
import mongoose.entities.Event;
import mongoose.entities.Label;
import mongoose.entities.Option;

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

    private OptionsPreselectionBuilder optionsPreselectionBuilder;

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

    private OptionsPreselectionBuilder getOptionsPreselectionBuilder() {
        if (optionsPreselectionBuilder == null)
            optionsPreselectionBuilder = new OptionsPreselectionBuilder(getEvent());
        return optionsPreselectionBuilder;
    }

    FeesGroupBuilder addDefaultOptions(Iterable<Option> options) {
        getOptionsPreselectionBuilder().addDefaultOptions(options);
        return this;
    }

    FeesGroupBuilder addAccommodationOption(Option option) {
        getOptionsPreselectionBuilder().addAccommodationOption(option);
        return this;
    }

    FeesGroup build() {
        return new FeesGroup(id, label, feesBottomLabel, feesPopupLabel, forceSoldout, getOptionsPreselectionBuilder().build());
    }
}
