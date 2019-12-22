package mongoose.client.controls.personaldetails;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.*;
import mongoose.client.activity.MongooseButtonFactoryMixin;
import mongoose.client.controls.sectionpanel.SectionPanelFactory;
import mongoose.client.services.authn.MongooseUserPrincipal;
import mongoose.client.validation.MongooseValidationSupport;
import mongoose.shared.domainmodel.formatters.DateFormatter;
import mongoose.shared.domainmodel.functions.AbcNames;
import mongoose.shared.entities.Country;
import mongoose.shared.entities.Event;
import mongoose.shared.entities.Organization;
import mongoose.shared.entities.Person;
import mongoose.shared.entities.markers.HasPersonalDetails;
import webfx.framework.client.services.i18n.I18n;
import webfx.framework.client.ui.controls.button.ButtonFactoryMixin;
import webfx.framework.client.ui.controls.entity.selector.EntityButtonSelector;
import webfx.framework.client.ui.controls.dialog.GridPaneBuilder;
import webfx.framework.client.ui.util.layout.LayoutUtil;
import webfx.extras.materialdesign.textfield.MaterialTextFieldPane;
import webfx.framework.client.ui.uirouter.uisession.UiSession;
import webfx.framework.shared.orm.domainmodel.DataSourceModel;
import webfx.framework.shared.orm.entity.Entity;
import webfx.framework.shared.orm.entity.EntityStore;
import webfx.extras.visual.controls.grid.VisualGrid;
import webfx.extras.visual.VisualColumn;
import webfx.extras.visual.VisualResultBuilder;
import webfx.extras.visual.VisualStyle;
import webfx.extras.visual.SelectionMode;
import webfx.extras.type.PrimType;
import webfx.kit.util.properties.Properties;
import webfx.platform.client.services.uischeduler.UiScheduler;
import webfx.platform.shared.util.Booleans;

import java.time.LocalDate;

/**
 * @author Bruno Salmon
 */
public final class PersonalDetailsPanel implements MongooseButtonFactoryMixin {

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
    private HasPersonalDetails model;
    private boolean editable = true;
    private final MongooseValidationSupport validationSupport = new MongooseValidationSupport();

    public PersonalDetailsPanel(Event event, ButtonFactoryMixin buttonFactoryMixin, Pane parent) {
        this(event, buttonFactoryMixin, parent, null);
    }

    public PersonalDetailsPanel(Event event, ButtonFactoryMixin buttonFactoryMixin, Pane parent, UiSession uiSession) {
        this.event = event;
        sectionPanel = SectionPanelFactory.createSectionPanel("YourPersonalDetails");

        firstNameTextField = newMaterialTextField("FirstName");
        lastNameTextField = newMaterialTextField("LastName");
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
        birthDatePicker.setConverter(DateFormatter.SINGLETON.toStringConverter());
        carer1NameTextField = newMaterialTextField("Carer1");
        carer2NameTextField = newMaterialTextField("Carer2");
        emailTextField = newMaterialTextField("Email");
        phoneTextField = newMaterialTextField("Phone");
        streetTextField = newMaterialTextField("Street");
        postCodeTextField = newMaterialTextField("Postcode");
        cityNameTextField = newMaterialTextField("City");
        DataSourceModel dataSourceModel = event.getStore().getDataSourceModel();
        countrySelector = createEntityButtonSelector("{class: 'Country', orderBy: 'name'}", buttonFactoryMixin, parent, dataSourceModel);
        countryButton = countrySelector.toMaterialButton("Country");
        organizationSelector = createEntityButtonSelector("{class: 'Organization', alias: 'o', where: '!closed and name!=`ISC`', orderBy: 'country.name,name'}", buttonFactoryMixin, parent, dataSourceModel);
        organizationButton = organizationSelector.toMaterialButton("Centre");
        if (uiSession == null) {
            personSelector = null;
            personButton = null;
        } else {
            personSelector = createEntityButtonSelector(null, buttonFactoryMixin, parent, dataSourceModel);
            personButton = personSelector.toMaterialButton("PersonToBook");
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
        boolean profileEditable = editable && (personSelector == null || personSelector.getSelectedItem() == null);
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
        countrySelector.setReadOnly(profileDisable);
        organizationSelector.setReadOnly(profileDisable);
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
                firstNameTextField,
                lastNameTextField,
                newMaterialRegion(genderBox, "Gender"),
                newMaterialRegion(ageBox, "Age")
        );
        if (personButton != null)
            vBox.getChildren().add(0, LayoutUtil.setUnmanagedWhenInvisible(personButton));
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
        VisualColumn keyColumn = VisualColumn.create(null, PrimType.STRING, VisualStyle.RIGHT_STYLE);
        VisualColumn valueColumn = VisualColumn.create(null, PrimType.STRING);
        VisualResultBuilder rsb = VisualResultBuilder.create(6, new VisualColumn[]{keyColumn, valueColumn, keyColumn, valueColumn});
        Organization organization = model.getOrganization();
        rsb.setValue(0, 0, I18n.getI18nText("FirstName:"));
        rsb.setValue(0, 1, model.getFirstName());
        rsb.setValue(1, 0, I18n.getI18nText("LastName:"));
        rsb.setValue(1, 1, model.getLastName());
        rsb.setValue(2, 0, I18n.getI18nText("Gender:"));
        rsb.setValue(2, 1, I18n.getI18nText(Booleans.isTrue(model.isMale()) ? "Male" : "Female"));
        rsb.setValue(3, 0, I18n.getI18nText("Age:"));
        rsb.setValue(3, 1, I18n.getI18nText(model.getAge() == null ? "Adult" : model.getAge()));
        rsb.setValue(4, 0, I18n.getI18nText("Email:"));
        rsb.setValue(4, 1, model.getEmail());
        rsb.setValue(5, 0, I18n.getI18nText("Phone:"));
        rsb.setValue(5, 1, model.getPhone());
        rsb.setValue(0, 2, I18n.getI18nText("Centre:"));
        rsb.setValue(0, 3, organization == null ? I18n.getI18nText("NoCentre") : organization.getName());
        rsb.setValue(1, 2, I18n.getI18nText("Street:"));
        rsb.setValue(1, 3, model.getStreet());
        rsb.setValue(2, 2, I18n.getI18nText("Postcode:"));
        rsb.setValue(2, 3, model.getPostCode());
        rsb.setValue(3, 2, I18n.getI18nText("City:"));
        rsb.setValue(3, 3, model.getCityName());
        rsb.setValue(4, 2, I18n.getI18nText("State:"));
        //rsb.setValue(5, 1, model.getPostCode());
        rsb.setValue(5, 2, I18n.getI18nText("Country:"));
        rsb.setValue(5, 3, model.getCountryName());
        VisualGrid visualGrid = new VisualGrid(rsb.build()); // LayoutUtil.setMinMaxHeightToPref(new DataGrid(rsb.build()));
        visualGrid.setHeaderVisible(false);
        visualGrid.setFullHeight(true);
        visualGrid.setSelectionMode(SelectionMode.DISABLED);
        return visualGrid;
    }

    public void syncUiFromModel(HasPersonalDetails p) {
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
            UiScheduler.runInUiThread(this::updatePanelBody);
    }

    public void syncModelFromUi(HasPersonalDetails p) {
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
