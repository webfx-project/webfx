package mongoose.client.businessdata.feesgroup;

import mongoose.client.businessdata.preselection.OptionsPreselection;
import mongoose.shared.entities.Event;
import mongoose.shared.entities.Label;
import mongoose.client.entities.util.Labels;
import webfx.platform.shared.util.Numbers;
import webfx.platform.shared.util.Objects;

/**
 * @author Bruno Salmon
 */
public final class FeesGroup {

    private final Event event;
    private final Object id;
    private final Label label;
    private final String i18nKey; // alternative i18n key if label is null
    private final Label feesBottomLabel;
    private final Label feesPopupLabel;
    private final boolean forceSoldout;
    private final OptionsPreselection[] optionsPreselections;

    FeesGroup(Event event, Object id, Label label, String i18nKey, Label feesBottomLabel, Label feesPopupLabel, boolean forceSoldout, OptionsPreselection[] optionsPreselections) {
        this.event = event;
        this.id = id;
        this.label = label;
        this.i18nKey = i18nKey != null ? i18nKey : "Fees";
        this.feesBottomLabel = feesBottomLabel;
        this.feesPopupLabel = feesPopupLabel;
        this.forceSoldout = forceSoldout;
        this.optionsPreselections = optionsPreselections;
    }

    public Event getEvent() {
        return event;
    }

    public Object getId() {
        return id;
    }

    public Label getLabel() {
        return label;
    }

    public Label getFeesBottomLabel() {
        return feesBottomLabel;
    }

    public Label getFeesPopupLabel() {
        return feesPopupLabel;
    }

    public boolean isForceSoldout() {
        return forceSoldout;
    }

    public OptionsPreselection[] getOptionsPreselections() {
        return optionsPreselections;
    }

    public String getDisplayName() {
        return Labels.instantTranslateLabel(label, i18nKey);
    }

    public String getDisplayName(Object language) {
        return Labels.instantTranslateLabel(label, language);
    }

    public String getFeesBottomText() {
        if (isInternationalFestival())
            return null;
        Label feesBottomLabel = Objects.coalesce(getFeesBottomLabel(), event.getFeesBottomLabel());
        return Labels.instantTranslateLabel(feesBottomLabel, "FeesExplanation");
    }

    public boolean isFestival() {
        return event.getName().toLowerCase().contains("festival");
    }

    private boolean isInternationalFestival() {
        return Numbers.intValue(event.getOrganizationId().getPrimaryKey()) == 1;
    }

    @Override
    public String toString() {
        return getDisplayName("en");
    }
}
