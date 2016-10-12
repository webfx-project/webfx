package mongoose.activities.frontend.event.fees;

import mongoose.activities.shared.highlevelcomponents.HighLevelComponents;
import mongoose.activities.shared.logic.preselection.OptionsPreselection;
import mongoose.entities.Label;
import mongoose.services.EventService;
import mongoose.util.Labels;
import naga.commons.type.PrimType;
import naga.commons.util.Numbers;
import naga.commons.util.Objects;
import naga.commons.util.async.Handler;
import naga.commons.util.tuples.Pair;
import naga.framework.ui.i18n.I18n;
import naga.toolkit.cell.renderers.TextRenderer;
import naga.toolkit.display.*;
import naga.toolkit.spi.Toolkit;
import naga.toolkit.spi.nodes.controls.Button;

/**
 * @author Bruno Salmon
 */
class FeesGroup {
    private final Object id;
    private final Label label;
    private final Label feesBottomLabel;
    private final Label feesPopupLabel;
    private final boolean forceSoldout;
    private final OptionsPreselection[] optionsPreselections;

    FeesGroup(Object id, Label label, Label feesBottomLabel, Label feesPopupLabel, boolean forceSoldout, OptionsPreselection[] optionsPreselections) {
        this.id = id;
        this.label = label;
        this.feesBottomLabel = feesBottomLabel;
        this.feesPopupLabel = feesPopupLabel;
        this.forceSoldout = forceSoldout;
        this.optionsPreselections = optionsPreselections;
    }

    Object getId() {
        return id;
    }

    Label getLabel() {
        return label;
    }

    Label getFeesBottomLabel() {
        return feesBottomLabel;
    }

    Label getFeesPopupLabel() {
        return feesPopupLabel;
    }

    boolean isForceSoldout() {
        return forceSoldout;
    }

    OptionsPreselection[] getOptionsPreselections() {
        return optionsPreselections;
    }

    String getDisplayName(I18n i18n) {
        return Labels.instantTranslateLabel(label, i18n, "Fees");
    }

    String getDisplayName(Object language) {
        return Labels.instantTranslateLabel(label, language);
    }

    DisplayResultSet generateDisplayResultSet(I18n i18n, EventService eventService, Handler<OptionsPreselection> bookHandler) {
        DisplayResultSetBuilder rsb = DisplayResultSetBuilder.create(optionsPreselections.length, new DisplayColumn[]{
                DisplayColumn.create(i18n.instantTranslate("Accommodation"), PrimType.STRING),
                DisplayColumn.create(i18n.instantTranslate("Fee"), PrimType.INTEGER, DisplayStyle.CENTER_STYLE),
                DisplayColumnBuilder.create(i18n.instantTranslate("Availability")).setStyle(DisplayStyle.CENTER_STYLE)
                        .setValueRenderer(p -> {
                            Pair<Object, OptionsPreselection> pair = (Pair<Object, OptionsPreselection>) p;
                            if (pair == null || !eventService.areEventAvailabilitiesLoaded())
                                return Toolkit.get().createImage("images/16/spinner.gif");
                            Object availability = pair.get1();
                            if (Numbers.isZero(availability))
                                return i18n.instantTranslateText(HighLevelComponents.createSoldoutButton(), "Soldout");
                            Button button = i18n.instantTranslateText(HighLevelComponents.createBookButton(), "Book");
                            button.actionEventObservable().subscribe(actionEvent -> bookHandler.handle(pair.get2()));
                            if (availability == null)
                                return button;
                            return Toolkit.get().createHBox(HighLevelComponents.createBadge(TextRenderer.SINGLETON.renderCellValue(availability)), button);
                        }).build()});
        int rowIndex = 0;
        for (OptionsPreselection optionsPreselection : optionsPreselections) {
            rsb.setValue(rowIndex,   0, optionsPreselection.getDisplayName(i18n));
            rsb.setValue(rowIndex,   1, optionsPreselection.getDisplayPrice(eventService));
            rsb.setValue(rowIndex++, 2, new Pair<>(optionsPreselection.getDisplayAvailability(eventService), optionsPreselection));
        }
        return rsb.build();
    }

    String getFeesBottomText(I18n i18n, EventService eventService) {
        Label feesBottomLabel = Objects.coalesce(getFeesBottomLabel(), eventService.getEvent().getFeesBottomLabel());
        return Labels.instantTranslateLabel(feesBottomLabel, i18n, "FeesExplanation");
    }

    @Override
    public String toString() {
        return getDisplayName("en");
    }
}
