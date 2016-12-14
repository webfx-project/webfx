package mongoose.activities.frontend.event.terms;

import mongoose.activities.frontend.event.shared.BookingsProcessViewModelBuilder;
import mongoose.activities.shared.highlevelcomponents.HighLevelComponents;
import naga.framework.ui.i18n.I18n;
import naga.toolkit.fx.ext.cell.collator.GridCollator;
import naga.toolkit.fx.scene.layout.BorderPane;
import naga.toolkit.spi.Toolkit;

/**
 * @author Bruno Salmon
 */
public class TermsViewModelBuilder extends BookingsProcessViewModelBuilder<TermsViewModel> {

    protected GridCollator termsLetterCollator;
    protected BorderPane termsPanel;

    @Override
    protected TermsViewModel createViewModel() {
        return new TermsViewModel(contentNode, termsLetterCollator, previousButton);
    }

    protected void buildComponents(Toolkit toolkit, I18n i18n) {
        super.buildComponents(toolkit, i18n);
        termsPanel = HighLevelComponents.createSectionPanel(null, "{url: 'images/certificate.svg', width: 16, height: 16}", "TermsAndConditions", i18n);
        termsPanel.setCenter(termsLetterCollator = new GridCollator("first", "first"));
        contentNode = BorderPane.create(termsPanel, null, null, previousButton, null);
    }
}
