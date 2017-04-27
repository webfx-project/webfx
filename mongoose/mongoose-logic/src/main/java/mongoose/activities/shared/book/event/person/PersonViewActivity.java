package mongoose.activities.shared.book.event.person;

import javafx.event.ActionEvent;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import mongoose.activities.shared.book.event.shared.BookingProcessViewActivity;
import mongoose.activities.shared.logic.ui.highlevelcomponents.HighLevelComponents;
import mongoose.activities.shared.logic.work.WorkingDocument;
import mongoose.entities.Document;
import naga.framework.ui.controls.GridPaneBuilder;
import naga.framework.ui.i18n.I18n;

/**
 * @author Bruno Salmon
 */
public class PersonViewActivity extends BookingProcessViewActivity {

    public PersonViewActivity() {
        super("summary");
    }

    private TextField firstNameTextField, lastNameTextField;
    @Override
    protected void createViewNodes() {
        super.createViewNodes();
        I18n i18n = getI18n();
        BorderPane personPanel = HighLevelComponents.createSectionPanel(null, null, "PersonalDetails", i18n);

        personPanel.setCenter(new GridPaneBuilder(i18n)
                .addLabelTextInputRow("FirstName", firstNameTextField = new TextField())
                .addLabelTextInputRow("LastName", lastNameTextField = new TextField())
                .getGridPane());

        ScrollPane scrollPane = new ScrollPane(personPanel);
        personPanel.prefWidthProperty().bind(scrollPane.widthProperty().subtract(16));
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        borderPane.setCenter(scrollPane);

        syncUiFromModel();
    }

    private Document editingDocument;

    private void syncUiFromModel() {
        WorkingDocument workingDocument = getWorkingDocument();
        Document document = workingDocument == null ? null : workingDocument.getDocument();
        if (firstNameTextField == null || editingDocument == document)
            return;
        editingDocument = document;
        firstNameTextField.setText(document.getFirstName());
        lastNameTextField.setText(document.getLastName());
    }

    private void syncModelFromUi() {
        Document document = editingDocument;
        document.setFirstName(firstNameTextField.getText());
        document.setLastName(lastNameTextField.getText());
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
