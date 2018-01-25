package mongoose.activities.shared.book.event.fees;

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
import mongoose.actions.MongooseIcons;
import mongoose.activities.shared.book.event.shared.BookingProcessViewActivity;
import mongoose.activities.shared.book.event.shared.FeesGroup;
import mongoose.activities.shared.logic.preselection.OptionsPreselection;
import mongoose.activities.shared.logic.ui.highlevelcomponents.HighLevelComponents;
import mongoose.activities.shared.logic.ui.highlevelcomponents.SectionPanelStyleOptions;
import mongoose.entities.Option;
import mongoose.entities.Person;
import naga.framework.orm.entity.EntityList;
import naga.framework.ui.i18n.Dictionary;
import naga.framework.ui.i18n.I18n;
import naga.framework.ui.layouts.LayoutUtil;
import naga.fx.properties.Properties;
import naga.fx.spi.Toolkit;
import naga.fxdata.cell.collator.GridCollator;
import naga.fxdata.control.DataGrid;
import naga.fxdata.control.SkinnedDataGrid;
import naga.fxdata.displaydata.*;
import naga.platform.json.Json;
import naga.platform.json.spi.JsonObject;
import naga.platform.json.spi.WritableJsonObject;
import naga.platform.services.log.spi.Logger;
import naga.type.SpecializedTextType;
import naga.util.Arrays;
import naga.util.Booleans;
import naga.util.tuples.Pair;

import static naga.framework.ui.controls.ImageViewUtil.createImageView;

/**
 * @author Bruno Salmon
 */
public class FeesViewActivity extends BookingProcessViewActivity {

    public FeesViewActivity() {
        super("options");
    }

    private GridCollator feesGroupsCollator;

    @Override
    protected void createViewNodes() {
        super.createViewNodes();
        feesGroupsCollator = new GridCollator(this::toFeesGroupPanel, nodes -> new VBox(20, nodes));
        verticalStack.getChildren().setAll(feesGroupsCollator, LayoutUtil.setMaxWidthToInfinite(backButton));

        feesGroupsCollator.displayResultSetProperty().bind(rsProperty);
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

        lastDictionary = getDictionary();
        Properties.consume(Properties.filter(Properties.combine(dictionaryProperty(), activeProperty(),
                Pair::new), // combine function
                pair -> pair.get2()), // filter function (GWT doesn't compile method reference in this case)
                pair -> refreshOnDictionaryChanged());
    }

    private Dictionary lastDictionary;

    private void refreshOnDictionaryChanged() {
        Dictionary newDictionary = getDictionary();
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

    private final Property<DisplayResultSet> rsProperty = new SimpleObjectProperty<>();
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
        Toolkit.get().scheduler().runOutUiThread(this::displayFeesGroupsNow);
    }

    private void displayFeesGroupsNow() {
        int n = feesGroups.length;
        DisplayResultSetBuilder rsb = DisplayResultSetBuilder.create(n, new DisplayColumn[]{
                DisplayColumn.create(value -> renderFeesGroupHeader((Pair<JsonObject, String>) value)),
                DisplayColumn.create(value -> renderFeesGroupBody((DisplayResultSet) value)),
                DisplayColumn.create(null, SpecializedTextType.HTML)});
        I18n i18n = getI18n();
        WritableJsonObject jsonImage = Json.parseObject(MongooseIcons.priceTagColorSvg16JsonUrl);
        ColumnWidthCumulator[] cumulators = {new ColumnWidthCumulator(), new ColumnWidthCumulator(), new ColumnWidthCumulator()};
        for (int i = 0; i < n; i++) {
            FeesGroup feesGroup = feesGroups[i];
            rsb.setValue(i, 0, new Pair<>(jsonImage, feesGroup.getDisplayName(i18n)));
            rsb.setValue(i, 1, feesGroup.generateDisplayResultSet(this, this, this::onBookButtonPressed, cumulators));
            if (i == n - 1) // Showing the fees bottom text only on the last fees group
                rsb.setValue(i, 2, feesGroup.getFeesBottomText(i18n));
        }
        DisplayResultSet rs = rsb.build();
        rsProperty.setValue(rs);
    }

    private Node renderFeesGroupHeader(Pair<JsonObject, String> pair) {
        boolean hasUnemployedRate = hasUnemployedRate();
        boolean hasFacilityFeeRate = hasFacilityFeeRate();
        boolean hasDiscountRates = hasUnemployedRate || hasFacilityFeeRate;
        RadioButton noDiscountRadio  = hasDiscountRates   ? instantTranslateText(new RadioButton(), "NoDiscount") : null;
        RadioButton unemployedRadio  = hasUnemployedRate  ? instantTranslateText(new RadioButton(), "UnemployedDiscount") : null;
        RadioButton facilityFeeRadio = hasFacilityFeeRate ? instantTranslateText(new RadioButton(), "FacilityFeeDiscount") : null;
        Person person = getPersonService().getPreselectionProfilePerson();
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

    private Node renderFeesGroupBody(DisplayResultSet rs) {
        DataGrid dataGrid = new SkinnedDataGrid(rs); //LayoutUtil.setMinMaxHeightToPref(new DataGrid(rs));
        dataGrid.setFullHeight(true);
        dataGrid.setSelectionMode(SelectionMode.DISABLED);
        return dataGrid;
    }

    private void onBookButtonPressed(OptionsPreselection optionsPreselection) {
        setSelectedOptionsPreselection(optionsPreselection);
        setWorkingDocument(null);
        onNextButtonPressed(null);
    }
}
