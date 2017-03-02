package mongoose.activities.shared.book.event.fees;

import javafx.beans.property.Property;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.RadioButton;
import javafx.scene.layout.FlowPane;
import javafx.scene.text.Text;
import mongoose.activities.shared.book.event.shared.BookingProcessPresentationLogicActivity;
import mongoose.activities.shared.book.event.shared.FeesGroup;
import mongoose.activities.shared.logic.preselection.OptionsPreselection;
import mongoose.entities.Option;
import mongoose.entities.Person;
import mongoose.services.PersonService;
import naga.commons.type.SpecializedTextType;
import naga.commons.util.Arrays;
import naga.commons.util.Booleans;
import naga.commons.util.tuples.Pair;
import naga.framework.orm.entity.EntityList;
import naga.framework.ui.i18n.Dictionary;
import naga.framework.ui.i18n.I18n;
import naga.fx.properties.Properties;
import naga.fx.spi.Toolkit;
import naga.fxdata.control.DataGrid;
import naga.fxdata.displaydata.DisplayColumn;
import naga.fxdata.displaydata.DisplayResultSet;
import naga.fxdata.displaydata.DisplayResultSetBuilder;
import naga.platform.json.Json;
import naga.platform.json.spi.JsonObject;
import naga.platform.json.spi.WritableJsonObject;
import naga.platform.spi.Platform;

import static naga.framework.ui.controls.ImageViewUtil.createImageView;

/**
 * @author Bruno Salmon
 */
public class FeesPresentationLogicActivity extends BookingProcessPresentationLogicActivity<FeesPresentationModel> {

    public FeesPresentationLogicActivity() {
        super(FeesPresentationModel::new, "options");
    }

    @Override
    protected void initializePresentationModel(FeesPresentationModel pm) {
        super.initializePresentationModel(pm);
        pm.setOnProgramAction(this::onProgramButtonPressed);
        pm.setOnTermsAction(this::onTermsButtonPressed);
    }

    private void onProgramButtonPressed(ActionEvent e) {
        goToNextBookingProcessPage("program");
    }

    private void onTermsButtonPressed(ActionEvent e) {
        goToNextBookingProcessPage("terms");
    }

    @Override
    public void onResume() {
        super.onResume();
        if (lastLoadedEventOptions != null && getEventOptions() != lastLoadedEventOptions)
            loadAndDisplayFeesGroups();
    }

    @Override
    protected void startLogic(FeesPresentationModel pm) {
        rsProperty = pm.dateInfoDisplayResultSetProperty();

        // Load and display fees groups now but also on event change
        Properties.runNowAndOnPropertiesChange(property -> loadAndDisplayFeesGroups(), pm.eventIdProperty());

        dictionary = getI18n().getDictionary();
        Properties.consume(Properties.filter(Properties.combine(getI18n().dictionaryProperty(), activeProperty(),
                Pair::new), // combine function
                pair -> pair.get2()), // filter function (GWT doesn't compile method reference in this case)
                pair -> refreshOnDictionaryChanged());
    }

    private Dictionary dictionary;

    private void refreshOnDictionaryChanged() {
        Dictionary dictionary = getI18n().getDictionary();
        if (this.dictionary != dictionary) {
            this.dictionary = dictionary;
            displayFeesGroups();
        }
    }

    private EntityList<Option> lastLoadedEventOptions;

    private void loadAndDisplayFeesGroups() {
        lastLoadedEventOptions = null;
        onFeesGroup().setHandler(ar -> {
            lastLoadedEventOptions = getEventOptions();
            if (ar.failed())
                Platform.log(ar.cause());
            else
                displayFeesGroupsAndRefreshAvailabilities(ar.result());
        });
    }

    private Property<DisplayResultSet> rsProperty;
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
        Toolkit.get().scheduler().runOutUiThread(() -> displayFeesGroupsNow());
    }

    private void displayFeesGroupsNow() {
        int n = feesGroups.length;
        DisplayResultSetBuilder rsb = DisplayResultSetBuilder.create(n, new DisplayColumn[]{
                DisplayColumn.create(value -> renderFeesGroupHeader((Pair<JsonObject, String>) value)),
                DisplayColumn.create(value -> renderFeesGroupBody((DisplayResultSet) value)),
                DisplayColumn.create(null, SpecializedTextType.HTML)});
        I18n i18n = getI18n();
        WritableJsonObject jsonImage = Json.parseObject("{url: 'images/price-tag.svg', width: 16, height: 16}");
        for (int i = 0; i < n; i++) {
            FeesGroup feesGroup = feesGroups[i];
            rsb.setValue(i, 0, new Pair<>(jsonImage, feesGroup.getDisplayName(i18n)));
            rsb.setValue(i, 1, feesGroup.generateDisplayResultSet(i18n, this, this::onBookButtonPressed));
            if (i == n - 1) // Showing the fees bottom text only on the last fees group
                rsb.setValue(i, 2, feesGroup.getFeesBottomText(i18n));
        }
        DisplayResultSet rs = rsb.build();
        rsProperty.setValue(rs);
    }

    private Node renderFeesGroupHeader(Pair<JsonObject, String> pair) {
        I18n i18n = getI18n();
        boolean hasUnemployedRate = hasUnemployedRate();
        boolean hasFacilityFeeRate = hasFacilityFeeRate();
        boolean hasDiscountRates = hasUnemployedRate || hasFacilityFeeRate;
        RadioButton noDiscountRadio  = hasDiscountRates ?   i18n.instantTranslateText(new RadioButton(), "NoDiscount") : null;
        RadioButton unemployedRadio  = hasUnemployedRate ?  i18n.instantTranslateText(new RadioButton(), "UnemployedDiscount") : null;
        RadioButton facilityFeeRadio = hasFacilityFeeRate ? i18n.instantTranslateText(new RadioButton(), "FacilityFeeDiscount") : null;
        PersonService personService = PersonService.get(getDataSourceModel());
        Person person = personService.getPreselectionProfilePerson();
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
        Text feesGroupText = new Text(pair.get2());
        Node[] nodes = {createImageView(pair.get1()), feesGroupText, noDiscountRadio, unemployedRadio, facilityFeeRadio};
        FlowPane header = new FlowPane(Arrays.nonNulls(Node[]::new, nodes));
        header.setHgap(5d);
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(new Insets(5));
        return header;
    }

    private Node renderFeesGroupBody(DisplayResultSet rs) {
        return new DataGrid(rs);
    }

    private void onBookButtonPressed(OptionsPreselection optionsPreselection) {
        setSelectedOptionsPreselection(optionsPreselection);
        setWorkingDocument(optionsPreselection.getWorkingDocument());
        onNextButtonPressed(null);
    }
}
