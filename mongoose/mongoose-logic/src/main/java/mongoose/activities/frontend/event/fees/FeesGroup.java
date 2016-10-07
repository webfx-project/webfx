package mongoose.activities.frontend.event.fees;

import mongoose.activities.shared.logic.preselection.OptionsPreselection;
import mongoose.entities.Label;

/**
 * @author Bruno Salmon
 */
class FeesGroup {
    private final Object id;
    private final Label label;
    private final Label feesBottomLabel;
    private final Label feesPopupLabel;
    private final boolean forceSoldout;
    private final OptionsPreselection optionsPreselection;

    public FeesGroup(Object id, Label label, Label feesBottomLabel, Label feesPopupLabel, boolean forceSoldout, OptionsPreselection optionsPreselection) {
        this.id = id;
        this.label = label;
        this.feesBottomLabel = feesBottomLabel;
        this.feesPopupLabel = feesPopupLabel;
        this.forceSoldout = forceSoldout;
        this.optionsPreselection = optionsPreselection;
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

    public OptionsPreselection getOptionsPreselection() {
        return optionsPreselection;
    }

    @Override
    public String toString() {
        return optionsPreselection.toString();
    }
}
