package mongoose.activities.frontend.event.terms;

import mongoose.activities.frontend.event.shared.BookingsProcessViewModelBuilder;
import mongoose.activities.shared.highlevelcomponents.HighLevelComponents;
import naga.framework.ui.i18n.I18n;
import naga.toolkit.cell.collators.GridCollator;
import naga.toolkit.spi.Toolkit;
import naga.toolkit.spi.nodes.layouts.VPage;

/**
 * @author Bruno Salmon
 */
public class TermsViewModelBuilder extends BookingsProcessViewModelBuilder<TermsViewModel> {

    protected GridCollator termsLetterCollator;
    protected VPage termsPanel;

    @Override
    protected TermsViewModel createViewModel() {
        return new TermsViewModel(contentNode, termsLetterCollator, previousButton);
    }

    protected void buildComponents(Toolkit toolkit, I18n i18n) {
        super.buildComponents(toolkit, i18n);
        termsLetterCollator = new GridCollator("vbox", "hbox");
        termsPanel = HighLevelComponents.createSectionPanel(null, "{url: 'images/certificate.svg', width: 16, height: 16}", "TermsAndConditions", i18n)
                .setCenter(termsLetterCollator);
        contentNode = toolkit.createVPage()
                .setCenter(termsPanel)
                .setFooter(previousButton);
    }
}
