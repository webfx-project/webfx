package mongoose.activities.shared.book.event.shared;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.*;
import mongoose.activities.shared.logic.ui.highlevelcomponents.HighLevelComponents;
import mongoose.domainmodel.format.DateFormatter;
import mongoose.entities.Country;
import mongoose.entities.Event;
import mongoose.entities.Organization;
import mongoose.entities.markers.HasPersonDetails;
import naga.commons.type.PrimType;
import naga.commons.util.Booleans;
import naga.framework.activity.view.ViewActivityContextMixin;
import naga.framework.orm.domainmodel.DataSourceModel;
import naga.framework.orm.entity.Entity;
import naga.framework.orm.entity.EntityStore;
import naga.framework.ui.controls.EntityButtonSelector;
import naga.framework.ui.controls.GridPaneBuilder;
import naga.framework.ui.controls.LayoutUtil;
import naga.framework.ui.i18n.I18n;
import naga.fx.properties.Properties;
import naga.fxdata.control.DataGrid;
import naga.fxdata.displaydata.DisplayColumn;
import naga.fxdata.displaydata.DisplayResultSetBuilder;
import naga.fxdata.displaydata.DisplayStyle;

import java.time.temporal.ChronoUnit;

/**
 * @author Bruno Salmon
 */
public class PersonDetailsPanel {
    private final Event event;
    private final I18n i18n;
    private final TextField firstNameTextField, lastNameTextField, carer1NameTextField, carer2NameTextField, emailTextField, phoneTextField, streetTextField, postCodeTextField, cityNameTextField;
    private final RadioButton maleRadioButton, femaleRadioButton, childRadioButton, adultRadioButton;
    private final DatePicker birthDatePicker;
    private final EntityButtonSelector countrySelector;
    private final EntityButtonSelector organizationSelector;
    private final BorderPane sectionPanel;
    private HasPersonDetails model;

    public PersonDetailsPanel(Event event, ViewActivityContextMixin viewActivityContextMixin, Pane parent) {
        this.event = event;
        i18n = viewActivityContextMixin.getI18n();
        sectionPanel = HighLevelComponents.createSectionPanel(null, null, "YourPersonalDetails", i18n);

        firstNameTextField = new TextField();
        lastNameTextField = new TextField();
        maleRadioButton = i18n.translateText(new RadioButton(),"Male");
        femaleRadioButton = i18n.translateText(new RadioButton(),"Female");
        ToggleGroup genderGroup = new ToggleGroup();
        maleRadioButton.setToggleGroup(genderGroup);
        femaleRadioButton.setToggleGroup(genderGroup);
        adultRadioButton = i18n.translateText(new RadioButton(),"Adult");
        childRadioButton = i18n.translateText(new RadioButton(),"Child");
        ToggleGroup ageGroup = new ToggleGroup();
        childRadioButton.setToggleGroup(ageGroup);
        adultRadioButton.setToggleGroup(ageGroup);
        birthDatePicker = LayoutUtil.setMaxWidthToInfinite(new DatePicker());
        carer1NameTextField = new TextField();
        carer2NameTextField = new TextField();
        emailTextField = new TextField();
        phoneTextField = new TextField();
        streetTextField = new TextField();
        postCodeTextField = new TextField();
        cityNameTextField = new TextField();
        DataSourceModel dataSourceModel = event.getStore().getDataSourceModel();
        countrySelector = new EntityButtonSelector("{class: 'Country', orderBy: 'name'}", viewActivityContextMixin, parent, dataSourceModel);
        organizationSelector = new EntityButtonSelector("{class: 'Organization', alias: 'o', where: '!closed and name!=`ISC`', orderBy: 'country.name,name'}", viewActivityContextMixin, parent, dataSourceModel);
    }

    public void setLoadingStore(EntityStore store) {
        countrySelector.setLoadingStore(store);
        organizationSelector.setLoadingStore(store);
    }

    public void setEditable(boolean editable) {
        boolean disable = !editable;
        firstNameTextField.setEditable(editable);
        lastNameTextField.setEditable(editable);
        maleRadioButton.setDisable(disable);
        femaleRadioButton.setDisable(disable);
        adultRadioButton.setDisable(disable);
        childRadioButton.setDisable(disable);
        birthDatePicker.setEditable(editable);
        carer1NameTextField.setEditable(editable);
        carer2NameTextField.setEditable(editable);
        emailTextField.setEditable(editable);
        phoneTextField.setEditable(editable);
        streetTextField.setEditable(editable);
        postCodeTextField.setEditable(editable);
        cityNameTextField.setEditable(editable);
        countrySelector.setEditable(editable);
        organizationSelector.setEditable(editable);
    }

    public BorderPane getSectionPanel() {
        return sectionPanel;
    }

    private void updatePanelBody() {
        sectionPanel.setCenter(createPanelBody());
    }

    private Node createPanelBody() {
        return firstNameTextField.isEditable() ? createPersonGridPane() : createPersonDataGrid();
    }


    private GridPane createPersonGridPane() {
        GridPaneBuilder gridPaneBuilder = new GridPaneBuilder(i18n)
                .addLabelTextInputRow("FirstName:", firstNameTextField)
                .addLabelTextInputRow("LastName:", lastNameTextField)
                .addLabelNodeRow("Gender:", new HBox(20, maleRadioButton, femaleRadioButton))
                .addLabelNodeRow("Age:", new HBox(20, adultRadioButton, childRadioButton));
        if (childRadioButton.isSelected())
            gridPaneBuilder
                    .addLabelNodeRow("BirthDate:", birthDatePicker)
                    .addLabelTextInputRow("Carer1:", carer1NameTextField)
                    .addLabelTextInputRow("Carer2:", carer2NameTextField);
        GridPane gridPane = gridPaneBuilder
                .addLabelTextInputRow("Email:", emailTextField)
                .addLabelTextInputRow("Phone:", phoneTextField)
                .addLabelTextInputRow("Street:", streetTextField)
                .addLabelTextInputRow("Postcode:", postCodeTextField)
                .addLabelTextInputRow("City:", cityNameTextField)
                .addLabelNodeRow("Country:", LayoutUtil.setMaxWidthToInfinite(countrySelector.getEntityButton()))
                .addLabelNodeRow("Centre:", LayoutUtil.setMaxWidthToInfinite(organizationSelector.getEntityButton()))
                .getGridPane();
        gridPane.setPadding(new Insets(10));
        return gridPane;
    }

    private Node createPersonDataGrid() {
        DisplayColumn keyColumn = DisplayColumn.create(null, PrimType.STRING, DisplayStyle.RIGHT_STYLE);
        DisplayColumn valueColumn = DisplayColumn.create(null, PrimType.STRING);
        DisplayResultSetBuilder rsb = DisplayResultSetBuilder.create(6, new DisplayColumn[]{keyColumn, valueColumn, keyColumn, valueColumn});
        rsb.setValue(0, 0, i18n.instantTranslate("FirstName:"));
        rsb.setValue(0, 1, model.getFirstName());
        rsb.setValue(0, 2, i18n.instantTranslate("LastName:"));
        rsb.setValue(0, 3, model.getLastName());
        rsb.setValue(1, 0, i18n.instantTranslate("Gender:"));
        rsb.setValue(1, 1, i18n.instantTranslate(model.isMale() ? "Male" : "Female"));
        rsb.setValue(1, 2, i18n.instantTranslate("Age:"));
        rsb.setValue(1, 3, i18n.instantTranslate(model.getAge() == null ? "Adult" : model.getAge()));
        rsb.setValue(2, 0, i18n.instantTranslate("Email:"));
        rsb.setValue(2, 1, model.getEmail());
        rsb.setValue(2, 2, i18n.instantTranslate("Phone:"));
        rsb.setValue(2, 3, model.getPhone());
        rsb.setValue(3, 0, i18n.instantTranslate("Centre:"));
        Organization organization = model.getOrganization();
        rsb.setValue(3, 1, organization == null ? i18n.instantTranslate("NoCentre") : organization.getName());
        rsb.setValue(3, 2, i18n.instantTranslate("Street:"));
        rsb.setValue(3, 3, model.getStreet());
        rsb.setValue(4, 0, i18n.instantTranslate("Postcode:"));
        rsb.setValue(4, 1, model.getPostCode());
        rsb.setValue(4, 2, i18n.instantTranslate("City:"));
        rsb.setValue(4, 3, model.getCityName());
        rsb.setValue(5, 0, i18n.instantTranslate("State:"));
        //rsb.setValue(5, 1, model.getPostCode());
        rsb.setValue(5, 2, i18n.instantTranslate("Country:"));
        rsb.setValue(5, 3, model.getCountryName());
        DataGrid dataGrid = new DataGrid(rsb.build());
        dataGrid.prefHeightProperty().bind(dataGrid.minHeightProperty());
        dataGrid.maxHeightProperty().bind(dataGrid.minHeightProperty());
        return new VBox(dataGrid); // VBox is only here to make the grid fit its height (see FxDataGridPeer)
    }

    public void syncUiFromModel(HasPersonDetails p) {
        model = p;
        if (p instanceof Entity)
            setLoadingStore(((Entity) p).getStore());
        firstNameTextField.setText(p.getFirstName());
        lastNameTextField.setText(p.getLastName());
        maleRadioButton.setSelected(Booleans.isTrue(p.isMale()));
        femaleRadioButton.setSelected(Booleans.isFalse(p.isMale()));
        Integer age = p.getAge();
        adultRadioButton.setSelected(age == null || age > 17);
        childRadioButton.setSelected((age != null && age <= 17));
        carer1NameTextField.setText(p.getCarer1Name());
        carer2NameTextField.setText(p.getCarer2Name());
        emailTextField.setText(p.getEmail());
        phoneTextField.setText(p.getPhone());
        streetTextField.setText(p.getStreet());
        postCodeTextField.setText(p.getPostCode());
        cityNameTextField.setText(p.getCityName());
        organizationSelector.setEntity(p.getOrganization());
        countrySelector.setEntity(p.getCountry());
        if (sectionPanel.getCenter() == null)
            Properties.runNowAndOnPropertiesChange(pty -> updatePanelBody(), childRadioButton.selectedProperty(), i18n.dictionaryProperty());
        if (!firstNameTextField.isEditable())
            updatePanelBody();
    }

    public void syncModelFromUi(HasPersonDetails p) {
        p.setFirstName(firstNameTextField.getText());
        p.setLastName(lastNameTextField.getText());
        p.setMale(maleRadioButton.isSelected());
        birthDatePicker.setConverter(DateFormatter.LOCAL_DATE_STRING_CONVERTER);
        Integer age = adultRadioButton.isSelected() ? null : (int) birthDatePicker.getValue().until(event.getStartDate(), ChronoUnit.YEARS);
        p.setAge(age);
        p.setCarer1Name(carer1NameTextField.getText());
        p.setCarer2Name(carer2NameTextField.getText());
        p.setEmail(emailTextField.getText());
        p.setPhone(phoneTextField.getText());
        p.setStreet(streetTextField.getText());
        p.setPostCode(postCodeTextField.getText());
        p.setCityName(cityNameTextField.getText());
        p.setOrganization(organizationSelector.getEntity());
        p.setCountry(countrySelector.getEntity());
        Country country = p.getCountry();
        p.setCountryName(country == null ? null : country.getName());
    }
}
