package mongoose.activities.shared.book.event.person;

import mongoose.activities.shared.book.event.shared.BookingProcessViewActivity;
import mongoose.activities.shared.book.event.shared.PersonDetailsPanel;
import mongoose.activities.shared.logic.work.WorkingDocument;
import naga.framework.ui.controls.LayoutUtil;

/**
 * @author Bruno Salmon
 */
public class PersonViewActivity extends BookingProcessViewActivity {

    public PersonViewActivity() {
        super("summary");
    }

    private PersonDetailsPanel personDetailsPanel;

    @Override
    protected void createViewNodes() {
        super.createViewNodes();
        personDetailsPanel = new PersonDetailsPanel(getEvent(), this, borderPane);

        borderPane.setCenter(LayoutUtil.createVerticalScrollPaneWithPadding(personDetailsPanel.getSectionPanel()));

        syncUiFromModel();
    }


    private void syncUiFromModel() {
        WorkingDocument workingDocument = getWorkingDocument();
        if (workingDocument != null)
            personDetailsPanel.syncUiFromModel(workingDocument.getDocument());
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
        // Clearing the computed price cache on leaving this page in case a personal detail affecting the price (such as age) has been changed
        WorkingDocument workingDocument = getWorkingDocument();
        if (workingDocument != null)
            workingDocument.clearComputedPrice();
    }
}
