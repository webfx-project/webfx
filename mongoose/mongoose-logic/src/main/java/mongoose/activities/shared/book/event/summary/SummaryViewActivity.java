package mongoose.activities.shared.book.event.summary;

import javafx.event.ActionEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import mongoose.activities.shared.book.event.shared.BookingOptionsPanel;
import mongoose.activities.shared.book.event.shared.BookingProcessViewActivity;
import mongoose.activities.shared.book.event.shared.PersonDetailsPanel;
import mongoose.activities.shared.logic.ui.highlevelcomponents.HighLevelComponents;
import mongoose.activities.shared.logic.work.WorkingDocument;
import naga.framework.ui.controls.LayoutUtil;
import naga.framework.ui.i18n.I18n;
import naga.platform.json.Json;
import naga.platform.spi.Platform;

import java.time.Instant;

/**
 * @author Bruno Salmon
 */
public class SummaryViewActivity extends BookingProcessViewActivity {

    private PersonDetailsPanel personDetailsPanel;
    private BookingOptionsPanel bookingOptionsPanel;

    public SummaryViewActivity() {
        super(null);
    }

    @Override
    protected void createViewNodes() {
        super.createViewNodes();
        I18n i18n = getI18n();
        personDetailsPanel = new PersonDetailsPanel(getEvent(), this, borderPane);
        personDetailsPanel.setEditable(false);
        bookingOptionsPanel = new BookingOptionsPanel(i18n);
        BorderPane requestPanel = HighLevelComponents.createSectionPanel(null, null, "SpecialRequest", i18n);
        BorderPane termsPanel = HighLevelComponents.createSectionPanel(null, null, "TermsAndConditions", i18n);
        VBox panelsVBox = new VBox(20, bookingOptionsPanel.getOptionsPanel(), personDetailsPanel.getSectionPanel(), requestPanel, termsPanel);

        borderPane.setCenter(LayoutUtil.createVerticalScrollPane(panelsVBox));
    }

    private void syncUiFromModel() {
        WorkingDocument workingDocument = getWorkingDocument();
        if (workingDocument != null) {
            bookingOptionsPanel.syncUiFromModel(workingDocument);
            personDetailsPanel.syncUiFromModel(workingDocument.getDocument());
        }
    }

    private void syncModelFromUi() {
        WorkingDocument workingDocument = getWorkingDocument();
        if (workingDocument != null)
            personDetailsPanel.syncModelFromUi(workingDocument.getDocument());
    }

    @Override
    public void onResume() {
        super.onResume();
        syncUiFromModel();
    }

    @Override
    public void onPause() {
        super.onPause();
        syncModelFromUi();
    }

    @Override
    protected void onNextButtonPressed(ActionEvent event) {
        getWorkingDocument().submit().setHandler(ar -> {
            if (ar.failed())
                Platform.log("Error submitting booking", ar.cause());
            else {
                //Platform.log("Document id = " + getWorkingDocument().getDocument().getId());
                getHistory().push("/event/" + getEvent().getPrimaryKey() + "/bookings", Json.createObject().set("refresh", Instant.now()));
            }
        });
    }
}
