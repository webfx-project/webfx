package mongoose.activities.shared.highlevelcomponents.impl;

import mongoose.activities.shared.highlevelcomponents.HighLevelComponentsFactory;
import mongoose.activities.shared.highlevelcomponents.SectionPanelStyleOptions;
import naga.toolkit.spi.Toolkit;
import naga.toolkit.spi.nodes.GuiNode;
import naga.toolkit.spi.nodes.controls.Button;
import naga.toolkit.spi.nodes.layouts.VPage;

/**
 * @author Bruno Salmon
 */
public class HighLevelComponentsFactoryImpl implements HighLevelComponentsFactory {

    @Override
    public VPage createSectionPanel(SectionPanelStyleOptions options) {
        return Toolkit.get().createVPage();
    }

    @Override
    public GuiNode createBadge(GuiNode... badgeNodes) {
        return Toolkit.get().createHBox(badgeNodes);
    }

    @Override
    public Button createBookButton() {
        return Toolkit.get().createButton();
    }

    @Override
    public Button createSoldoutButton() {
        return Toolkit.get().createButton();
    }
}
