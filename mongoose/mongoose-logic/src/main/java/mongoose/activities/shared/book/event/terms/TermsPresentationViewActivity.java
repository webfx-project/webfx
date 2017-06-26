package mongoose.activities.shared.book.event.terms;

import javafx.scene.Node;
import javafx.scene.layout.BorderPane;
import mongoose.activities.shared.book.event.shared.BookingProcessPresentationViewActivity;
import naga.framework.ui.controls.LayoutUtil;
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
        termsPanel = createSectionPanel("{url: 'images/certificate.svg', width: 16, height: 16}", "TermsAndConditions");
        termsPanel.setCenter(LayoutUtil.createVerticalScrollPaneWithPadding(termsLetterCollator));

        termsLetterCollator.displayResultSetProperty().bind(pm.termsLetterDisplayResultSetProperty());
    }

    @Override
    protected Node assemblyViewNodes() {
        return new BorderPane(termsPanel, null, null, previousButton, null);
    }
}
