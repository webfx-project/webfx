package mongoose.activities.frontend.event.terms;

import javafx.scene.Node;
import javafx.scene.layout.BorderPane;
import mongoose.activities.frontend.event.shared.BookingProcessPresentationViewActivity;
import mongoose.activities.shared.highlevelcomponents.HighLevelComponents;
import naga.fxdata.cell.collator.GridCollator;

/**
 * @author Bruno Salmon
 */
public class TermsPresentationViewActivity extends BookingProcessPresentationViewActivity<TermsPresentationModel> {

    private BorderPane termsPanel;

    @Override
    protected void createViewNodes(TermsPresentationModel pm) {
        super.createViewNodes(pm);
        GridCollator termsLetterCollator = new GridCollator("first", "first");
        termsPanel = HighLevelComponents.createSectionPanel(null, "{url: 'images/certificate.svg', width: 16, height: 16}", "TermsAndConditions", getI18n());
        termsPanel.setCenter(termsLetterCollator);

        termsLetterCollator.displayResultSetProperty().bind(pm.termsLetterDisplayResultSetProperty());
    }

    @Override
    protected Node assemblyViewNodes() {
        return new BorderPane(termsPanel, null, null, previousButton, null);
    }
}
