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

    VPage createSectionPanel(SectionPanelStyleOptions options);

    default VPage createSectionPanel(SectionPanelStyleOptions options, String iconImageUrl, String translationKey, I18n i18n) {
        return createSectionPanel(options, PresentationActivity.createImage(iconImageUrl), PresentationActivity.createTextView(translationKey, i18n));
    }

    default VPage createSectionPanel(SectionPanelStyleOptions options, GuiNode... headerNodes) {
        return createSectionPanel(options).setHeader(Toolkit.get().createHBox(headerNodes));
    }

    GuiNode createBadge(GuiNode... badgeNodes);
}
