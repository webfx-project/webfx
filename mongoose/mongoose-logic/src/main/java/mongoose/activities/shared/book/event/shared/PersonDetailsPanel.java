package mongoose.activities.shared.book.event.shared;

import javafx.geometry.Insets;
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import mongoose.activities.shared.logic.ui.highlevelcomponents.HighLevelComponents;
import mongoose.domainmodel.format.DateFormatter;
import mongoose.entities.Event;
import mongoose.entities.markers.HasPersonDetails;
import naga.commons.util.Booleans;
import naga.framework.activity.view.ViewActivityContextMixin;
import naga.framework.orm.domainmodel.DataSourceModel;
import naga.framework.orm.entity.EntityStore;
import naga.framework.ui.controls.EntityButtonSelector;
import naga.framework.ui.controls.GridPaneBuilder;
import naga.framework.ui.controls.LayoutUtil;
import naga.framework.ui.i18n.I18n;
import naga.fx.properties.Properties;

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

    public PersonDetailsPanel(Event event, ViewActivityContextMixin viewActivityContextMixin, Pane parent) {
        this(event, event.getStore(), viewActivityContextMixin, parent);
    }

    public PersonDetailsPanel(Event event, EntityStore store, ViewActivityContextMixin viewActivityContextMixin, Pane parent) {
        this.event = event;
        i18n = viewActivityContextMixin.getI18n();
        sectionPanel = HighLevelComponents.createSectionPanel(null, null, "YourPersonalDetails", i18n);

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
        DataSourceModel dataSourceModel = store.getDataSourceModel();
        countrySelector = new EntityButtonSelector("{class: 'Country', orderBy: 'name'}", viewActivityContextMixin, parent, dataSourceModel);
        organizationSelector = new EntityButtonSelector("{class: 'Organization', alias: 'o', where: '!closed and name!=`ISC`', orderBy: 'country.name,name'}", viewActivityContextMixin, parent, dataSourceModel);
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

    private GridPane createPersonGridPane() {
        GridPaneBuilder gridPaneBuilder = new GridPaneBuilder(i18n)
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

    public void syncUiFromModel(HasPersonDetails p) {
/*
        if (p instanceof Entity) {
            EntityStore store = ((Entity) p).getStore();
            countrySelector.setEntityStore(store);
            organizationSelector.setEntityStore(store);
        }
*/
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
            Properties.runNowAndOnPropertiesChange(pty -> sectionPanel.setCenter(createPersonGridPane()), childRadioButton.selectedProperty());
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
    }
}
