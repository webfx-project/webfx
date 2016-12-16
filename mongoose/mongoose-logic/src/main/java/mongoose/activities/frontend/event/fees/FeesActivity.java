package mongoose.activities.frontend.event.fees;

import javafx.beans.property.Property;
import mongoose.activities.frontend.event.shared.BookingProcessActivity;
import mongoose.activities.frontend.event.shared.FeesGroup;
import mongoose.activities.shared.logic.preselection.OptionsPreselection;
import mongoose.entities.Person;
import mongoose.services.PersonService;
import naga.commons.type.SpecializedTextType;
import naga.commons.util.Arrays;
import naga.commons.util.Booleans;
import naga.commons.util.tuples.Pair;
import naga.framework.ui.i18n.I18n;
import naga.platform.json.Json;
import naga.platform.json.spi.JsonObject;
import naga.platform.json.spi.WritableJsonObject;
import naga.platform.spi.Platform;
import naga.toolkit.display.DisplayColumn;
import naga.toolkit.display.DisplayResultSet;
import naga.toolkit.display.DisplayResultSetBuilder;
import naga.toolkit.fx.ext.control.DataGrid;
import naga.toolkit.fx.geometry.Insets;
import naga.toolkit.fx.geometry.Pos;
import naga.toolkit.fx.scene.Node;
import naga.toolkit.fx.scene.control.RadioButton;
import naga.toolkit.fx.scene.layout.FlowPane;
import naga.toolkit.fx.scene.text.Text;
import naga.toolkit.spi.events.MouseEvent;
import naga.toolkit.util.Properties;

/**
 * @author Bruno Salmon
 */
public class FeesActivity extends BookingProcessActivity<FeesViewModel, FeesPresentationModel> {

    public FeesActivity() {
        super(FeesPresentationModel::new, "options");
        registerViewBuilder(getClass(), new FeesViewModelBuilder());
    }

    @Override
    protected void bindViewModelWithPresentationModel(FeesViewModel vm, FeesPresentationModel pm) {
        super.bindViewModelWithPresentationModel(vm, pm);
        I18n i18n = getI18n();
        i18n.translateText(setGraphic(vm.getProgramButton(), "{url: 'images/calendar.svg', width: 16, height: 16}"), "Program")
                .setOnMouseClicked(this::onProgramButtonPressed);
        i18n.translateText(setGraphic(vm.getTermsButton(), "{url: 'images/certificate.svg', width: 16, height: 16}"), "TermsAndConditions")
                .setOnMouseClicked(this::onTermsButtonPressed);
        vm.getDateInfoCollator().displayResultSetProperty().bind(pm.dateInfoDisplayResultSetProperty());
    }

    private void onProgramButtonPressed(MouseEvent e) {
        goToNextBookingProcessPage("program");
    }

    private void onTermsButtonPressed(MouseEvent e) {
        goToNextBookingProcessPage("terms");
    }

    protected void bindPresentationModelWithLogic(FeesPresentationModel pm) {
        // Load and display fees groups now but also on event change
        Properties.runNowAndOnPropertiesChange(property -> loadAndDisplayFeesGroups(pm), pm.eventIdProperty());
    }

    private void loadAndDisplayFeesGroups(FeesPresentationModel pm) {
        onFeesGroup().setHandler(async -> {
            if (async.failed())
                Platform.log(async.cause());
            else {
                FeesGroup[] feesGroups = async.result();
                Property<DisplayResultSet> dateInfoDisplayResultSetProperty = pm.dateInfoDisplayResultSetProperty();
                I18n i18n = getI18n();
                Properties.consumeInUiThread(Properties.filter(Properties.combine(i18n.dictionaryProperty(), activeProperty(),
                        Pair::new), // combine function
                        pair -> pair.get2()), // filter function (GWT doesn't compile method reference in this case)
                        pair -> displayFeesGroups(feesGroups, dateInfoDisplayResultSetProperty));
                onEventAvailabilities().setHandler(ar -> {
                    if (ar.succeeded())
                        displayFeesGroups(feesGroups, dateInfoDisplayResultSetProperty);
                });
            }
        });
    }

    private void displayFeesGroups(FeesGroup[] feesGroups, Property<DisplayResultSet> dateInfoDisplayResultSetProperty) {
        if (getEvent() == null) // This can happen when reacting to active property while the event has just changed and is not yet loaded
            return; // We return to avoid NPE (this method will be called again once the event is loaded)
        int n = feesGroups.length;
        DisplayResultSetBuilder rsb = DisplayResultSetBuilder.create(n, new DisplayColumn[]{
                DisplayColumn.createFx(value -> renderFeesGroupHeader((Pair<JsonObject, String>) value, feesGroups, dateInfoDisplayResultSetProperty)),
                DisplayColumn.createFx(value -> renderFeesGroupBody((DisplayResultSet) value)),
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
        dateInfoDisplayResultSetProperty.setValue(rs);
    }

    private Node renderFeesGroupHeader(Pair<JsonObject, String> pair, FeesGroup[] feesGroups, Property<DisplayResultSet> dateInfoDisplayResultSetProperty) {
        I18n i18n = getI18n();
        boolean hasUnemployedRate = hasUnemployedRate();
        boolean hasFacilityFeeRate = hasFacilityFeeRate();
        boolean hasDiscountRates = hasUnemployedRate || hasFacilityFeeRate;
        RadioButton noDiscountRadio  = hasDiscountRates ?   i18n.instantTranslateText(RadioButton.create(), "NoDiscount") : null;
        RadioButton unemployedRadio  = hasUnemployedRate ?  i18n.instantTranslateText(RadioButton.create(), "UnemployedDiscount") : null;
        RadioButton facilityFeeRadio = hasFacilityFeeRate ? i18n.instantTranslateText(RadioButton.create(), "FacilityFeeDiscount") : null;
        PersonService personService = PersonService.get(getDataSourceModel());
        Person person = personService.getPreselectionProfilePerson();
        if (unemployedRadio != null) {
            unemployedRadio.setSelected(Booleans.isTrue(person.isUnemployed()));
            unemployedRadio.selectedProperty().addListener((observable, oldValue, unemployed) -> {
                person.setUnemployed(unemployed);
                if (unemployed)
                    person.setFacilityFee(false);
                displayFeesGroups(feesGroups, dateInfoDisplayResultSetProperty);
            });
        }
        if (facilityFeeRadio != null) {
            facilityFeeRadio.setSelected(Booleans.isTrue(person.isFacilityFee()));
            facilityFeeRadio.selectedProperty().addListener((observable, oldValue, facilityFee) -> {
                person.setFacilityFee(facilityFee);
                if (facilityFee)
                    person.setUnemployed(false);
                displayFeesGroups(feesGroups, dateInfoDisplayResultSetProperty);
            });
        }
        if (noDiscountRadio != null) {
            noDiscountRadio.setSelected(Booleans.isNotTrue(person.isUnemployed()) && Booleans.isNotTrue(person.isFacilityFee()));
            noDiscountRadio.selectedProperty().addListener((observable, oldValue, noDiscount) -> {
                if (noDiscount) {
                    person.setUnemployed(false);
                    person.setFacilityFee(false);
                }
                displayFeesGroups(feesGroups, dateInfoDisplayResultSetProperty);
            });
        }
        Text feesGroupText = Text.create(pair.get2());
        Node[] nodes = {createImageView(pair.get1()), feesGroupText, noDiscountRadio, unemployedRadio, facilityFeeRadio};
        FlowPane header = FlowPane.create(Arrays.nonNulls(Node[]::new, nodes));
        header.setHgap(5d);
        header.setAlignment(Pos.CENTER_LEFT);
        header.setInsets(Insets.create(5, 5, 5, 5));
        return header;
    }

    private Node renderFeesGroupBody(DisplayResultSet rs) {
        return DataGrid.create(rs);
    }

    private void onBookButtonPressed(OptionsPreselection optionsPreselection) {
        setSelectedOptionsPreselection(optionsPreselection);
        setWorkingDocument(optionsPreselection.getWorkingDocument());
        onNextButtonPressed(null);
    }
}
