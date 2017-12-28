package mongoose.activities.shared.book.event.shared;

import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.HBox;
import mongoose.actions.MongooseIcons;
import mongoose.activities.shared.logic.preselection.OptionsPreselection;
import mongoose.activities.shared.logic.ui.highlevelcomponents.HighLevelComponents;
import mongoose.entities.Event;
import mongoose.entities.Label;
import mongoose.services.EventService;
import mongoose.util.Labels;
import naga.type.PrimType;
import naga.util.Numbers;
import naga.util.Objects;
import naga.util.async.Handler;
import naga.util.tuples.Pair;
import naga.framework.ui.controls.BackgroundUtil;
import naga.framework.ui.i18n.I18n;
import naga.fx.spi.Toolkit;
import naga.fx.util.ImageStore;
import naga.fxdata.cell.collator.NodeCollatorRegistry;
import naga.fxdata.cell.renderer.TextRenderer;
import naga.fxdata.displaydata.*;

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

    public String getDisplayName(I18n i18n) {
        return Labels.instantTranslateLabel(label, i18n, i18nKey);
    }

    public String getDisplayName(Object language) {
        return Labels.instantTranslateLabel(label, language);
    }

    public DisplayResultSet generateDisplayResultSet(I18n i18n, EventService eventService, Handler<OptionsPreselection> bookHandler) {
        boolean showBadges = Objects.areEquals(eventService.getEvent().getOrganizationId().getPrimaryKey(), 2); // For now only showing badges on KMCF courses
        int optionsCount = optionsPreselections.length;
        boolean singleOption = optionsCount == 1;
        DisplayResultSetBuilder rsb = DisplayResultSetBuilder.create(optionsCount, new DisplayColumn[]{
                DisplayColumn.create(i18n.instantTranslate(singleOption ? "Course" : "Accommodation"), PrimType.STRING),
                DisplayColumn.create(i18n.instantTranslate("Fee"), PrimType.INTEGER, DisplayStyle.CENTER_STYLE),
                DisplayColumnBuilder.create(i18n.instantTranslate("Availability")).setStyle(DisplayStyle.CENTER_STYLE)
                        .setValueRenderer(p -> {
                            Pair<Object, OptionsPreselection> pair = (Pair<Object, OptionsPreselection>) p;
                            if (pair == null || !eventService.areEventAvailabilitiesLoaded())
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
                                return i18n.instantTranslateText(HighLevelComponents.createSoldoutButton(), "Soldout");
                            Button button = i18n.instantTranslateText(HighLevelComponents.createBookButton(), "Book>>");
                            button.setOnAction(e -> {
                                // Creating a press down effect (inverting the background gradient) especially for low-end mobiles
                                // where several seconds can happen before next page is displayed (at least they know click is happening)
                                Background background = button.getBackground();
                                button.setBackground(BackgroundUtil.newVerticalLinearGradientBackground("#2a8236", "#7fd504", 5));
                                Toolkit.get().scheduler().schedulePropertyChangeInNextAnimationFrame(() -> {
                                    Toolkit.get().scheduler().schedulePropertyChangeInNextAnimationFrame(() -> {
                                        Platform.runLater(() -> {
                                            bookHandler.handle(optionsPreselection);
                                            button.setBackground(background);
                                        });
                                    });
                                });
                            });
                            if (availability == null || !showBadges)
                                return button;
                            HBox hBox = (HBox) NodeCollatorRegistry.hBoxCollator().collateNodes(HighLevelComponents.createBadge(TextRenderer.SINGLETON.renderCellValue(availability)), button);
                            hBox.setAlignment(Pos.CENTER);
                            return hBox;
                        }).build()});
        int rowIndex = 0;
        for (OptionsPreselection optionsPreselection : optionsPreselections) {
            rsb.setValue(rowIndex,   0, singleOption ? /* Showing course name instead of 'NoAccommodation' when single line */ Labels.instantTranslateLabel(Objects.coalesce(label, Labels.bestLabelOrName(event)), i18n) : /* Otherwise showing accommodation type */ optionsPreselection.getDisplayName(i18n));
            rsb.setValue(rowIndex,   1, optionsPreselection.getDisplayPrice());
            rsb.setValue(rowIndex++, 2, new Pair<>(optionsPreselection.getDisplayAvailability(eventService), optionsPreselection));
        }
        return rsb.build();
    }

    public String getFeesBottomText(I18n i18n) {
        Label feesBottomLabel = Objects.coalesce(getFeesBottomLabel(), event.getFeesBottomLabel());
        return Labels.instantTranslateLabel(feesBottomLabel, i18n, "FeesExplanation");
    }

    @Override
    public String toString() {
        return getDisplayName("en");
    }
}
