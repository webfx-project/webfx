package mongoose.activities.bothends.book.shared;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.*;
import mongoose.activities.bothends.generic.MongooseButtonFactoryMixin;
import mongoose.activities.bothends.generic.MongooseSectionFactoryMixin;
import mongoose.activities.bothends.logic.ui.validation.MongooseValidationSupport;
import mongoose.services.authn.MongooseUserPrincipal;
import mongoose.domainmodel.formatters.DateFormatter;
import mongoose.domainmodel.functions.AbcNames;
import mongoose.entities.Country;
import mongoose.entities.Event;
import mongoose.entities.Organization;
import mongoose.entities.Person;
import mongoose.entities.markers.HasPersonDetails;
import naga.framework.activity.base.elementals.view.ViewActivityContextMixin;
import naga.framework.orm.domainmodel.DataSourceModel;
import naga.framework.orm.entity.Entity;
import naga.framework.orm.entity.EntityStore;
import naga.framework.services.i18n.I18n;
import naga.framework.ui.graphic.controls.button.ButtonFactoryMixin;
import naga.framework.ui.graphic.controls.button.EntityButtonSelector;
import naga.framework.ui.graphic.controls.dialog.GridPaneBuilder;
import naga.framework.ui.graphic.materialdesign.textfield.MaterialTextFieldPane;
import naga.framework.ui.layouts.LayoutUtil;
import naga.framework.ui.uisession.UiSession;
import naga.fx.properties.Properties;
import naga.fx.spi.Toolkit;
import naga.fxdata.control.DataGrid;
import naga.fxdata.displaydata.DisplayColumn;
import naga.fxdata.displaydata.DisplayResultBuilder;
import naga.fxdata.displaydata.DisplayStyle;
import naga.fxdata.displaydata.SelectionMode;
import naga.type.PrimType;
import naga.util.Booleans;

import java.time.LocalDate;

/**
 * @author Bruno Salmon
 */
public class PersonDetailsPanel implements MongooseButtonFactoryMixin, MongooseSectionFactoryMixin {

    private static final int CHILD_MAX_AGE = 17;

    private final Event event;
    private final TextField firstNameTextField, lastNameTextField, carer1NameTextField, carer2NameTextField, emailTextField, phoneTextField, streetTextField, postCodeTextField, cityNameTextField;
    private final RadioButton maleRadioButton, femaleRadioButton, childRadioButton, adultRadioButton;
    private final HBox genderBox, ageBox;
    private final DatePicker birthDatePicker;
    private final EntityButtonSelector<Person> personSelector;
    private final EntityButtonSelector<Country> countrySelector;
    private final EntityButtonSelector<Organization> organizationSelector;
    private final MaterialTextFieldPane personButton, countryButton, organizationButton;
    private final BorderPane sectionPanel;
    private HasPersonDetails model;
    private boolean editable = true;
    private final MongooseValidationSupport validationSupport = new MongooseValidationSupport();

    public PersonDetailsPanel(Event event, ViewActivityContextMixin viewActivityContextMixin, Pane parent) {
        this(event, viewActivityContextMixin, parent, null);
    }

    public PersonDetailsPanel(Event event, ViewActivityContextMixin viewActivityContextMixin, Pane parent, UiSession uiSession) {
        this.event = event;
        sectionPanel = createSectionPanel("YourPersonalDetails");

        firstNameTextField = newMaterialTextField("FirstName", "FirstNamePlaceholder");
        lastNameTextField = newMaterialTextField("LastName", "LastNamePlaceholder");
        maleRadioButton = newRadioButton("Male");
        femaleRadioButton = newRadioButton("Female");
        ToggleGroup genderGroup = new ToggleGroup();
        maleRadioButton.setToggleGroup(genderGroup);
        femaleRadioButton.setToggleGroup(genderGroup);
        genderBox = new HBox(20, maleRadioButton, femaleRadioButton);
        adultRadioButton = newRadioButton("Adult");
        childRadioButton = newRadioButton("Child");
        ToggleGroup ageGroup = new ToggleGroup();
        childRadioButton.setToggleGroup(ageGroup);
        adultRadioButton.setToggleGroup(ageGroup);
        ageBox = new HBox(20, adultRadioButton, childRadioButton);
        birthDatePicker = LayoutUtil.setMaxWidthToInfinite(new DatePicker());
        birthDatePicker.setConverter(DateFormatter.LOCAL_DATE_STRING_CONVERTER);
        carer1NameTextField = newMaterialTextField("Carer1", "Carer1Placeholder");
        carer2NameTextField = newMaterialTextField("Carer2", "Carer2Placeholder");
        emailTextField = newMaterialTextField("Email", "EmailPlaceholder");
        phoneTextField = newMaterialTextField("Phone", "PhonePlaceholder");
        streetTextField = newMaterialTextField("Street", "StreetPlaceholder");
        postCodeTextField = newMaterialTextField("Postcode", "PostcodePlaceholder");
        cityNameTextField = newMaterialTextField("City", "CityPlaceholder");
        DataSourceModel dataSourceModel = event.getStore().getDataSourceModel();
        countrySelector = createEntityButtonSelector("{class: 'Country', orderBy: 'name'}", viewActivityContextMixin, parent, dataSourceModel);
        countrySelector.setSearchEnabled(true);
        countryButton = countrySelector.toMaterialButton("Country", "CountryPlaceholder");
        organizationSelector = createEntityButtonSelector("{class: 'Organization', alias: 'o', where: '!closed and name!=`ISC`', orderBy: 'country.name,name'}", viewActivityContextMixin, parent, dataSourceModel);
        organizationSelector.setSearchEnabled(true);
        organizationButton = organizationSelector.toMaterialButton("Centre", "CentreInfo");
        if (uiSession == null) {
            personSelector = null;
            personButton = null;
        } else {
            personSelector = createEntityButtonSelector(null, viewActivityContextMixin, parent, dataSourceModel);
            personButton = personSelector.toMaterialButton("PersonToBook", null);
            Properties.runOnPropertiesChange(p -> syncUiFromModel((Person) p.getValue()), personSelector.selectedItemProperty());
            Properties.runNowAndOnPropertiesChange(userProperty -> {
                Object user = userProperty.getValue();
                boolean loggedIn = user instanceof MongooseUserPrincipal;
                if (loggedIn) {
                    Object userAccountId = ((MongooseUserPrincipal) user).getUserAccountId();
                    personSelector.setJsonOrClass("{class: 'Person', alias: 'p', fields: 'genderIcon,firstName,lastName,birthdate,email,phone,street,postCode,cityName,organization,country', columns: `[{expression: 'genderIcon,firstName,lastName'}]`, where: '!removed and frontendAccount=" + userAccountId + "', orderBy: 'id'}");
                    personSelector.autoSelectFirstEntity();
                } else
                    personSelector.setJsonOrClass(null);
                personButton.setVisible(loggedIn);
            }, uiSession.userPrincipalProperty());
        }
        initValidation();
    }

    private void initValidation() {
        validationSupport.addRequiredInputs(firstNameTextField, lastNameTextField);
        validationSupport.addRequiredInput(maleRadioButton.getToggleGroup().selectedToggleProperty(), genderBox);
        validationSupport.addRequiredInputs(emailTextField, phoneTextField, carer1NameTextField, carer2NameTextField);
        validationSupport.addRequiredInput(countrySelector.selectedItemProperty(), countrySelector.getButton());
    }

    public boolean isValid() {
        return validationSupport.isValid();
    }

    private static <T extends Entity> EntityButtonSelector<T> createEntityButtonSelector(Object jsonOrClass, ButtonFactoryMixin buttonFactory, Pane parent, DataSourceModel dataSourceModel) {
        return new EntityButtonSelector<T>(jsonOrClass, buttonFactory, parent, dataSourceModel) {
            @Override
            protected void setSearchParameters(String search, EntityStore store) {
                super.setSearchParameters(search, store);
                store.setParameterValue("abcSearchLike", AbcNames.evaluate(search, true));
            }
        };
    }

    public void setLoadingStore(EntityStore store) {
        countrySelector.setLoadingStore(store);
        organizationSelector.setLoadingStore(store);
    }

    public void setEditable(boolean editable) {
        this.editable = editable;
        updateUiEditable();
    }

    private void updateUiEditable() {
        boolean profileEditable = editable && personSelector.getSelectedItem() == null;
        boolean profileDisable = !profileEditable;
        firstNameTextField.setEditable(profileEditable);
        lastNameTextField.setEditable(profileEditable);
        maleRadioButton.setDisable(profileDisable);
        femaleRadioButton.setDisable(profileDisable);
        adultRadioButton.setDisable(profileDisable);
        childRadioButton.setDisable(profileDisable);
        birthDatePicker.setEditable(profileEditable);
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
        return editable ? createPersonVBox() /*createPersonGridPane()*/ : createPersonDataGrid();
    }

    private GridPane createPersonGridPane() {
        GridPaneBuilder gridPaneBuilder = new GridPaneBuilder()
                .addLabelNodeRow("PersonToBook:", personButton)
                .addLabelTextInputRow("FirstName:", firstNameTextField)
                .addLabelTextInputRow("LastName:", lastNameTextField)
                .addLabelNodeRow("Gender:", genderBox)
                .addLabelNodeRow("Age:", ageBox);
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
                .addLabelNodeRow("Country:", countryButton)
                .addLabelNodeRow("Centre:", organizationButton)
                .build();
        gridPane.setPadding(new Insets(10));
        return gridPane;
    }

    private VBox createPersonVBox() {
        VBox vBox = new VBox(15,
                LayoutUtil.setUnmanagedWhenInvisible(personButton),
                firstNameTextField,
                lastNameTextField,
                newMaterialRegion(genderBox, "Gender"),
                newMaterialRegion(ageBox, "Age")
        );
        if (childRadioButton.isSelected())
            vBox.getChildren().addAll(
                    newMaterialRegion(birthDatePicker, "BirthDate"),
                    carer1NameTextField,
                    carer2NameTextField
            );
        vBox.getChildren().addAll(
                emailTextField,
                phoneTextField,
                streetTextField,
                postCodeTextField,
                cityNameTextField,
                countryButton,
                organizationButton
        );
        return LayoutUtil.setPadding(vBox, 10, 18);
    }

    private Node createPersonDataGrid() {
        DisplayColumn keyColumn = DisplayColumn.create(null, PrimType.STRING, DisplayStyle.RIGHT_STYLE);
        DisplayColumn valueColumn = DisplayColumn.create(null, PrimType.STRING);
        DisplayResultBuilder rsb = DisplayResultBuilder.create(6, new DisplayColumn[]{keyColumn, valueColumn, keyColumn, valueColumn});
        Organization organization = model.getOrganization();
        rsb.setValue(0, 0, I18n.instantTranslate("FirstName:"));
        rsb.setValue(0, 1, model.getFirstName());
        rsb.setValue(1, 0, I18n.instantTranslate("LastName:"));
        rsb.setValue(1, 1, model.getLastName());
        rsb.setValue(2, 0, I18n.instantTranslate("Gender:"));
        rsb.setValue(2, 1, I18n.instantTranslate(Booleans.isTrue(model.isMale()) ? "Male" : "Female"));
        rsb.setValue(3, 0, I18n.instantTranslate("Age:"));
        rsb.setValue(3, 1, I18n.instantTranslate(model.getAge() == null ? "Adult" : model.getAge()));
        rsb.setValue(4, 0, I18n.instantTranslate("Email:"));
        rsb.setValue(4, 1, model.getEmail());
        rsb.setValue(5, 0, I18n.instantTranslate("Phone:"));
        rsb.setValue(5, 1, model.getPhone());
        rsb.setValue(0, 2, I18n.instantTranslate("Centre:"));
        rsb.setValue(0, 3, organization == null ? I18n.instantTranslate("NoCentre") : organization.getName());
        rsb.setValue(1, 2, I18n.instantTranslate("Street:"));
        rsb.setValue(1, 3, model.getStreet());
        rsb.setValue(2, 2, I18n.instantTranslate("Postcode:"));
        rsb.setValue(2, 3, model.getPostCode());
        rsb.setValue(3, 2, I18n.instantTranslate("City:"));
        rsb.setValue(3, 3, model.getCityName());
        rsb.setValue(4, 2, I18n.instantTranslate("State:"));
        //rsb.setValue(5, 1, model.getPostCode());
        rsb.setValue(5, 2, I18n.instantTranslate("Country:"));
        rsb.setValue(5, 3, model.getCountryName());
        DataGrid dataGrid = new DataGrid(rsb.build()); // LayoutUtil.setMinMaxHeightToPref(new DataGrid(rsb.build()));
        dataGrid.setHeaderVisible(false);
        dataGrid.setFullHeight(true);
        dataGrid.setSelectionMode(SelectionMode.DISABLED);
        return dataGrid;
    }

    public void syncUiFromModel(HasPersonDetails p) {
        model = p;
        if (p instanceof Entity)
            setLoadingStore(((Entity) p).getStore());
        firstNameTextField.setText(p.getFirstName());
        lastNameTextField.setText(p.getLastName());
        maleRadioButton.setSelected(Booleans.isTrue(p.isMale()));
        femaleRadioButton.setSelected(Booleans.isFalse(p.isMale()));
        LocalDate birthDate = null;
        if (p instanceof Person) {
            Person person = (Person) p;
            birthDate = person.getBirthDate();
            person.setAge(computeAge(birthDate));
        }
        birthDatePicker.setValue(birthDate);
        Integer age = p.getAge();
        adultRadioButton.setSelected(age == null || age > CHILD_MAX_AGE);
        childRadioButton.setSelected((age != null && age <= CHILD_MAX_AGE));
        carer1NameTextField.setText(p.getCarer1Name());
        carer2NameTextField.setText(p.getCarer2Name());
        emailTextField.setText(p.getEmail());
        phoneTextField.setText(p.getPhone());
        streetTextField.setText(p.getStreet());
        postCodeTextField.setText(p.getPostCode());
        cityNameTextField.setText(p.getCityName());
        organizationSelector.setSelectedItem(p.getOrganization());
        countrySelector.setSelectedItem(p.getCountry());
        updateUiEditable();
        if (sectionPanel.getCenter() == null)
            Properties.runNowAndOnPropertiesChange(this::updatePanelBody, childRadioButton.selectedProperty(), I18n.dictionaryProperty());
        if (!editable)
            Toolkit.get().scheduler().runInUiThread(this::updatePanelBody);
    }

    public void syncModelFromUi(HasPersonDetails p) {
        p.setFirstName(firstNameTextField.getText());
        p.setLastName(lastNameTextField.getText());
        p.setMale(maleRadioButton.isSelected());
        p.setAge(childRadioButton.isSelected() ? computeAge(birthDatePicker.getValue()) : null);
        p.setCarer1Name(carer1NameTextField.getText());
        p.setCarer2Name(carer2NameTextField.getText());
        p.setEmail(emailTextField.getText());
        p.setPhone(phoneTextField.getText());
        p.setStreet(streetTextField.getText());
        p.setPostCode(postCodeTextField.getText());
        p.setCityName(cityNameTextField.getText());
        p.setOrganization(organizationSelector.getSelectedItem());
        p.setCountry(countrySelector.getSelectedItem());
        Country country = p.getCountry();
        p.setCountryName(country == null ? null : country.getName());
    }

    private Integer computeAge(LocalDate birthDate) {
        Integer age = null;
        if (birthDate != null) {
            // Integer age = (int) birthDate.until(event.getStartDate(), ChronoUnit.YEARS); // Doesn't compile with GWT
            age = (int) (event.getStartDate().toEpochDay() - birthDate.toEpochDay()) / 365;
            if (age > CHILD_MAX_AGE) // TODO: move this later in a applyBusinessRules() method
                age = null;
        }
        return age;
    }
}
