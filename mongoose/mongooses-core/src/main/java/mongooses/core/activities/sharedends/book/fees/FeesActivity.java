package mongooses.core.activities.sharedends.book.fees;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import mongooses.core.actions.MongooseIcons;
import mongooses.core.activities.sharedends.book.shared.BookingProcessActivity;
import mongooses.core.activities.sharedends.book.shared.FeesGroup;
import mongooses.core.activities.sharedends.logic.preselection.OptionsPreselection;
import mongooses.core.activities.sharedends.logic.ui.highlevelcomponents.HighLevelComponents;
import mongooses.core.activities.sharedends.logic.ui.highlevelcomponents.SectionPanelStyleOptions;
import mongooses.core.entities.Option;
import mongooses.core.entities.Person;
import mongooses.core.operations.bothends.route.RouteToOptionsRequest;
import webfx.framework.orm.entity.EntityList;
import webfx.framework.services.i18n.Dictionary;
import webfx.framework.services.i18n.I18n;
import webfx.framework.ui.layouts.LayoutUtil;
import webfx.fxkits.core.properties.Properties;
import webfx.fxkits.extra.cell.collator.GridCollator;
import webfx.fxkits.extra.control.DataGrid;
import webfx.fxkits.extra.control.SkinnedDataGrid;
import webfx.fxkits.extra.displaydata.*;
import webfx.fxkits.extra.type.SpecializedTextType;
import webfx.platforms.core.services.json.Json;
import webfx.platforms.core.services.json.JsonObject;
import webfx.platforms.core.services.json.WritableJsonObject;
import webfx.platforms.core.services.log.Logger;
import webfx.platforms.core.services.uischeduler.UiScheduler;
import webfx.platforms.core.util.Arrays;
import webfx.platforms.core.util.Booleans;
import webfx.platforms.core.util.tuples.Pair;

import static webfx.framework.ui.graphic.image.JsonImageViews.createImageView;

/**
 * @author Bruno Salmon
 */
final class FeesActivity extends BookingProcessActivity {

    private GridCollator feesGroupsCollator;

    @Override
    protected void createViewNodes() {
        super.createViewNodes();
        feesGroupsCollator = new GridCollator(this::toFeesGroupPanel, nodes -> new VBox(20, nodes));
        verticalStack.getChildren().setAll(feesGroupsCollator, LayoutUtil.setMaxWidthToInfinite(backButton));

        feesGroupsCollator.displayResultProperty().bind(rsProperty);
    }

    private Node toFeesGroupPanel(Node... nodes) {
        BorderPane borderPane = buildFeesSectionPanel(nodes[0]);
        borderPane.setCenter(nodes[1]);
        borderPane.setBottom(nodes[2]);
        return borderPane;
    }

    private BorderPane buildFeesSectionPanel(Node node) {
        SectionPanelStyleOptions options = new SectionPanelStyleOptions(false);
        return HighLevelComponents.createSectionPanel(options, node);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (lastLoadedEventOptions != null && getEventOptions() != lastLoadedEventOptions)
            loadAndDisplayFeesGroups();
    }

    @Override
    protected void startLogic() {
        // Load and display fees groups now but also on event change
        Properties.runNowAndOnPropertiesChange(this::loadAndDisplayFeesGroups, eventIdProperty());

        lastDictionary = I18n.getDictionary();
        Properties.consume(Properties.filter(Properties.combine(I18n.dictionaryProperty(), activeProperty(),
                Pair::new), // combine function
                pair -> pair.get2()), // filter function (GWT doesn't compile method reference in this case)
                pair -> refreshOnDictionaryChanged());
    }

    private Dictionary lastDictionary;

    private void refreshOnDictionaryChanged() {
        Dictionary newDictionary = I18n.getDictionary();
        if (lastDictionary != newDictionary) {
            lastDictionary = newDictionary;
            displayFeesGroups();
        }
    }

    private EntityList<Option> lastLoadedEventOptions;

    private void loadAndDisplayFeesGroups() {
        lastLoadedEventOptions = null;
        onFeesGroups().setHandler(ar -> {
            lastLoadedEventOptions = getEventOptions();
            if (ar.failed())
                Logger.log(ar.cause());
            else
                displayFeesGroupsAndRefreshAvailabilities(ar.result());
        });
    }

    private final Property<DisplayResult> rsProperty = new SimpleObjectProperty<>();
    private FeesGroup[] feesGroups;

    private void displayFeesGroupsAndRefreshAvailabilities(FeesGroup[] feesGroups) {
        this.feesGroups = feesGroups;
        onEventAvailabilities().setHandler(ar -> {
            if (ar.succeeded())
                displayFeesGroups();
        });
        if (getEventAvailabilities() == null)
            displayFeesGroups();
    }

    private void displayFeesGroups() {
        if (getEvent() == null || feesGroups == null) // This can happen when reacting to active property while the event has just changed and is not yet loaded
            return; // We return to avoid NPE (this method will be called again once the event is loaded)
        UiScheduler.runOutUiThread(this::displayFeesGroupsNow);
    }

    private void displayFeesGroupsNow() {
        int n = feesGroups.length;
        DisplayResultBuilder rsb = DisplayResultBuilder.create(n, new DisplayColumn[]{
                DisplayColumn.create((value, context) -> renderFeesGroupHeader((Pair<JsonObject, String>) value)),
                DisplayColumn.create((value, context) -> renderFeesGroupBody((DisplayResult) value)),
                DisplayColumn.create(null, SpecializedTextType.HTML)});
        WritableJsonObject jsonImage = Json.parseObject(MongooseIcons.priceTagColorSvg16JsonUrl);
        ColumnWidthCumulator[] cumulators = {new ColumnWidthCumulator(), new ColumnWidthCumulator(), new ColumnWidthCumulator()};
        for (int i = 0; i < n; i++) {
            FeesGroup feesGroup = feesGroups[i];
            rsb.setValue(i, 0, new Pair<>(jsonImage, feesGroup.getDisplayName()));
            rsb.setValue(i, 1, feesGroup.generateDisplayResult(this, this, this::onBookButtonPressed, cumulators));
            if (i == n - 1) // Showing the fees bottom text only on the last fees group
                rsb.setValue(i, 2, feesGroup.getFeesBottomText());
        }
        DisplayResult rs = rsb.build();
        rsProperty.setValue(rs);
    }

    private Node renderFeesGroupHeader(Pair<JsonObject, String> pair) {
        boolean hasUnemployedRate = hasUnemployedRate();
        boolean hasFacilityFeeRate = hasFacilityFeeRate();
        boolean hasDiscountRates = hasUnemployedRate || hasFacilityFeeRate;
        RadioButton noDiscountRadio  = hasDiscountRates   ? I18n.instantTranslateText(new RadioButton(), "NoDiscount") : null;
        RadioButton unemployedRadio  = hasUnemployedRate  ? I18n.instantTranslateText(new RadioButton(), "UnemployedDiscount") : null;
        RadioButton facilityFeeRadio = hasFacilityFeeRate ? I18n.instantTranslateText(new RadioButton(), "FacilityFeeDiscount") : null;
        Person person = getPersonAggregate().getPreselectionProfilePerson();
        if (unemployedRadio != null) {
            unemployedRadio.setSelected(Booleans.isTrue(person.isUnemployed()));
            unemployedRadio.selectedProperty().addListener((observable, oldValue, unemployed) -> {
                person.setUnemployed(unemployed);
                if (unemployed)
                    person.setFacilityFee(false);
                displayFeesGroups();
            });
        }
        if (facilityFeeRadio != null) {
            facilityFeeRadio.setSelected(Booleans.isTrue(person.isFacilityFee()));
            facilityFeeRadio.selectedProperty().addListener((observable, oldValue, facilityFee) -> {
                person.setFacilityFee(facilityFee);
                if (facilityFee)
                    person.setUnemployed(false);
                displayFeesGroups();
            });
        }
        if (noDiscountRadio != null) {
            noDiscountRadio.setSelected(Booleans.isNotTrue(person.isUnemployed()) && Booleans.isNotTrue(person.isFacilityFee()));
            noDiscountRadio.selectedProperty().addListener((observable, oldValue, noDiscount) -> {
                if (noDiscount) {
                    person.setUnemployed(false);
                    person.setFacilityFee(false);
                }
                displayFeesGroups();
            });
        }
        Label feesGroupLabel = new Label(pair.get2());
        Node[] nodes = {createImageView(pair.get1()), feesGroupLabel, noDiscountRadio, unemployedRadio, facilityFeeRadio};
        FlowPane header = new FlowPane(Arrays.nonNulls(Node[]::new, nodes));
        header.setHgap(5d);
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(new Insets(5));
        return header;
    }

    private Node renderFeesGroupBody(DisplayResult rs) {
        DataGrid dataGrid = new SkinnedDataGrid(rs); //LayoutUtil.setMinMaxHeightToPref(new DataGrid(rs));
        dataGrid.setFullHeight(true);
        dataGrid.setSelectionMode(SelectionMode.DISABLED);
        return dataGrid;
    }

    private void onBookButtonPressed(OptionsPreselection optionsPreselection) {
        new RouteToOptionsRequest(optionsPreselection, getHistory()).execute();
    }
}
