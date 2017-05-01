package mongoose.activities.shared.book.event.person;

import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import mongoose.activities.shared.book.event.shared.BookingProcessViewActivity;
import mongoose.activities.shared.logic.ui.highlevelcomponents.HighLevelComponents;
import mongoose.activities.shared.logic.work.WorkingDocument;
import mongoose.domainmodel.format.DateFormatter;
import mongoose.entities.Document;
import naga.commons.util.Booleans;
import naga.framework.ui.controls.GridPaneBuilder;
import naga.framework.ui.controls.LayoutUtil;
import naga.framework.ui.i18n.I18n;
import naga.fx.properties.Properties;

import java.time.temporal.ChronoUnit;

/**
 * @author Bruno Salmon
 */
public class PersonViewActivity extends BookingProcessViewActivity {

    public PersonViewActivity() {
        super("summary");
    }

    private TextField firstNameTextField, lastNameTextField, carer1NameTextField, carer2NameTextField, emailTextField, phoneTextField, streetTextField, postCodeTextField, cityNameTextField;
    private RadioButton maleRadioButton, femaleRadioButton, childRadioButton, adultRadioButton;
    private DatePicker birthDatePicker;
    private EntitySelector countrySelector;
    private EntitySelector organizationSelector;

    @Override
    protected void createViewNodes() {
        super.createViewNodes();
        I18n i18n = getI18n();
        BorderPane personPanel = HighLevelComponents.createSectionPanel(null, null, "PersonalDetails", i18n);

        firstNameTextField = new TextField();
        lastNameTextField = new TextField();
        maleRadioButton = new RadioButton("Male");
        femaleRadioButton = new RadioButton("Female");
        ToggleGroup genderGroup = new ToggleGroup();
        maleRadioButton.setToggleGroup(genderGroup);
        femaleRadioButton.setToggleGroup(genderGroup);
        adultRadioButton = new RadioButton("Adult");
        childRadioButton = new RadioButton("Child");
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
        countrySelector = new EntitySelector("{class: 'Country', orderBy: 'name'}", this, borderPane, getDataSourceModel(), getI18n());
        organizationSelector = new EntitySelector("{class: 'Organization', alias: 'o', where: '!closed and name!=`ISC`', orderBy: 'country.name,name'}", this, borderPane, getDataSourceModel(), getI18n());

        ScrollPane scrollPane = new ScrollPane(personPanel);
        personPanel.prefWidthProperty().bind(scrollPane.widthProperty().subtract(16));
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        borderPane.setCenter(scrollPane);

        syncUiFromModel();
        Properties.runNowAndOnPropertiesChange(p -> personPanel.setCenter(createPersonGridPane()), childRadioButton.selectedProperty());
    }

    private GridPane createPersonGridPane() {
        GridPaneBuilder gridPaneBuilder = new GridPaneBuilder(getI18n())
                .addLabelTextInputRow("FirstName", firstNameTextField)
                .addLabelTextInputRow("LastName", lastNameTextField)
                .addLabelNodeRow("Gender", new HBox(20, maleRadioButton, femaleRadioButton))
                .addLabelNodeRow("Age", new HBox(20, adultRadioButton, childRadioButton));
        if (childRadioButton.isSelected())
            gridPaneBuilder
                    .addLabelNodeRow("BirthDate", birthDatePicker)
                    .addLabelTextInputRow("Carer1", carer1NameTextField)
                    .addLabelTextInputRow("Carer2", carer2NameTextField);
        GridPane gridPane = gridPaneBuilder
                .addLabelTextInputRow("Email", emailTextField)
                .addLabelTextInputRow("Phone", phoneTextField)
                .addLabelTextInputRow("Street", streetTextField)
                .addLabelTextInputRow("PostCode", postCodeTextField)
                .addLabelTextInputRow("City", cityNameTextField)
                .addLabelNodeRow("Country", LayoutUtil.setMaxWidthToInfinite(countrySelector.getEntityButton()))
                .addLabelNodeRow("Centre", LayoutUtil.setMaxWidthToInfinite(organizationSelector.getEntityButton()))
                .getGridPane();
        gridPane.setPadding(new Insets(10));
        return gridPane;
    }

    private Document editingDocument;

    private void syncUiFromModel() {
        WorkingDocument workingDocument = getWorkingDocument();
        Document document = workingDocument == null ? null : workingDocument.getDocument();
        if (firstNameTextField == null || document == null || editingDocument == document)
            return;
        editingDocument = document;
        firstNameTextField.setText(document.getFirstName());
        lastNameTextField.setText(document.getLastName());
        maleRadioButton.setSelected(Booleans.isTrue(document.isMale()));
        femaleRadioButton.setSelected(Booleans.isFalse(document.isMale()));
        Integer age = document.getAge();
        adultRadioButton.setSelected(age == null || age > 17);
        childRadioButton.setSelected((age != null && age <= 17));
        carer1NameTextField.setText(document.getCarer1Name());
        carer2NameTextField.setText(document.getCarer2Name());
        emailTextField.setText(document.getEmail());
        phoneTextField.setText(document.getPhone());
        streetTextField.setText(document.getStreet());
        postCodeTextField.setText(document.getPostCode());
        cityNameTextField.setText(document.getCityName());
        organizationSelector.setEntity(document.getOrganization());
        countrySelector.setEntity(document.getCountry());
    }

    private void syncModelFromUi() {
        Document document = editingDocument;
        document.setFirstName(firstNameTextField.getText());
        document.setLastName(lastNameTextField.getText());
        document.setMale(maleRadioButton.isSelected());
        birthDatePicker.setConverter(DateFormatter.LOCAL_DATE_STRING_CONVERTER);
        Integer age = adultRadioButton.isSelected() ? null : (int) birthDatePicker.getValue().until(getEvent().getStartDate(), ChronoUnit.YEARS);
        document.setAge(age);
        document.setCarer1Name(carer1NameTextField.getText());
        document.setCarer2Name(carer2NameTextField.getText());
        document.setEmail(emailTextField.getText());
        document.setPhone(phoneTextField.getText());
        document.setStreet(streetTextField.getText());
        document.setPostCode(postCodeTextField.getText());
        document.setCityName(cityNameTextField.getText());
        document.setOrganization(organizationSelector.getEntity());
        document.setCountry(countrySelector.getEntity());
    }

    @Override
    public void onResume() {
        super.onResume();
        syncUiFromModel();
    }

    @Override
    protected void onNextButtonPressed(ActionEvent event) {
        syncModelFromUi();
        super.onNextButtonPressed(event);
    }
}
