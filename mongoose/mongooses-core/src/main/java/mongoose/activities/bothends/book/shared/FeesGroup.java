package mongoose.activities.bothends.book.shared;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import mongoose.actions.MongooseIcons;
import mongoose.activities.bothends.generic.MongooseButtonFactoryMixin;
import mongoose.activities.bothends.logic.preselection.OptionsPreselection;
import mongoose.activities.bothends.logic.ui.highlevelcomponents.HighLevelComponents;
import mongoose.aggregates.EventAggregate;
import mongoose.entities.Event;
import mongoose.entities.Label;
import mongoose.util.Labels;
import webfx.framework.services.i18n.I18n;
import webfx.fx.util.ImageStore;
import webfx.fxdata.cell.collator.NodeCollatorRegistry;
import webfx.fxdata.cell.renderer.TextRenderer;
import webfx.fxdata.cell.renderer.ValueRenderingContext;
import webfx.fxdata.displaydata.*;
import webfx.type.PrimType;
import webfx.util.Numbers;
import webfx.util.Objects;
import webfx.util.async.Handler;
import webfx.util.tuples.Pair;

/**
 * @author Bruno Salmon
 */
public class FeesGroup {
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

    public DisplayResult generateDisplayResult(MongooseButtonFactoryMixin buttonFactory, EventAggregate eventAggregate, Handler<OptionsPreselection> bookHandler, ColumnWidthCumulator[] cumulators) {
        boolean showBadges = Objects.areEquals(eventAggregate.getEvent().getOrganizationId().getPrimaryKey(), 2); // For now only showing badges on KMCF courses
        int optionsCount = optionsPreselections.length;
        boolean singleOption = optionsCount == 1;
        DisplayResultBuilder rsb = DisplayResultBuilder.create(optionsCount, new DisplayColumn[]{
                DisplayColumnBuilder.create(I18n.instantTranslate(singleOption ? (isFestival() ? "Festival" : "Course") : "Accommodation"), PrimType.STRING).setCumulator(cumulators[0]).build(),
                DisplayColumnBuilder.create(I18n.instantTranslate("Fee"), PrimType.INTEGER).setStyle(DisplayStyle.CENTER_STYLE).setCumulator(cumulators[1]).build(),
                DisplayColumnBuilder.create(I18n.instantTranslate("Availability")).setStyle(DisplayStyle.CENTER_STYLE).setCumulator(cumulators[2])
                        .setValueRenderer((p, context) -> {
                            Pair<Object, OptionsPreselection> pair = (Pair<Object, OptionsPreselection>) p;
                            if (pair == null || !eventAggregate.areEventAvailabilitiesLoaded())
                                return new ImageView(ImageStore.getOrCreateImage(MongooseIcons.spinnerIcon16Url, 16, 16));
                            Object availability = pair.get1();
                            OptionsPreselection optionsPreselection = pair.get2();
                            // Availability is null when there is no online room at all. In this case...
                            if (availability == null && optionsPreselection.hasAccommodationExcludingSharing()) // ... if it's an accommodation option (but not just sharing)
                                availability = 0; // we show it as sold out - otherwise (if it's a sharing option or no accommodation) we show it as available
                            boolean soldout = availability != null && Numbers.doubleValue(availability) <= 0 || // Showing sold out if the availability is zero
                                    optionsPreselection.isForceSoldout() || // or if the option has been forced as sold out in the backend
                                    isForceSoldout(); // or if the whole FeesGroup has been forced as sold out
                            if (soldout)
                                return buttonFactory.newSoldoutButton();
                            Button button = buttonFactory.newBookButton();
                            button.setOnAction(e -> bookHandler.handle(optionsPreselection));
                            if (availability == null || !showBadges)
                                return button;
                            HBox hBox = (HBox) NodeCollatorRegistry.hBoxCollator().collateNodes(HighLevelComponents.createBadge(TextRenderer.SINGLETON.renderValue(availability, ValueRenderingContext.DEFAULT_READONLY_CONTEXT)), button);
                            hBox.setAlignment(Pos.CENTER);
                            return hBox;
                        }).build()});
        int rowIndex = 0;
        for (OptionsPreselection optionsPreselection : optionsPreselections) {
            rsb.setValue(rowIndex,   0, singleOption ? /* Showing course name instead of 'NoAccommodation' when single line */ Labels.instantTranslateLabel(Objects.coalesce(label, Labels.bestLabelOrName(event))) : /* Otherwise showing accommodation type */ optionsPreselection.getDisplayName());
            rsb.setValue(rowIndex,   1, optionsPreselection.getDisplayPrice());
            rsb.setValue(rowIndex++, 2, new Pair<>(optionsPreselection.getDisplayAvailability(eventAggregate), optionsPreselection));
        }
        return rsb.build();
    }

    public String getFeesBottomText() {
        if (isInternationalFestival())
            return null;
        Label feesBottomLabel = Objects.coalesce(getFeesBottomLabel(), event.getFeesBottomLabel());
        return Labels.instantTranslateLabel(feesBottomLabel, "FeesExplanation");
    }

    private boolean isFestival() {
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
