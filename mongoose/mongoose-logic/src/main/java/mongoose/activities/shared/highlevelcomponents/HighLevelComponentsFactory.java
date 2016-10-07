package mongoose.activities.shared.highlevelcomponents;

import naga.framework.ui.i18n.I18n;
import naga.framework.ui.presentation.PresentationActivity;
import naga.toolkit.spi.Toolkit;
import naga.toolkit.spi.nodes.GuiNode;
import naga.toolkit.spi.nodes.layouts.VPage;

/**
 * @author Bruno Salmon
 */
public interface HighLevelComponentsFactory {

    VPage createSectionPanel();

    default VPage createSectionPanel(String iconImageUrl, String translationKey, I18n i18n) {
        return createSectionPanel(PresentationActivity.createImage(iconImageUrl), PresentationActivity.createTextView(translationKey, i18n));
    }

    default VPage createSectionPanel(GuiNode... headerNodes) {
        return createSectionPanel().setHeader(Toolkit.get().createHBox(headerNodes));
    }
}
