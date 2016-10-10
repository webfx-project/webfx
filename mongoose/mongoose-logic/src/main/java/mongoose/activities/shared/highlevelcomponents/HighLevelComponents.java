package mongoose.activities.shared.highlevelcomponents;

import mongoose.activities.shared.highlevelcomponents.impl.HighLevelComponentsFactoryImpl;
import naga.framework.ui.i18n.I18n;
import naga.toolkit.spi.nodes.GuiNode;
import naga.toolkit.spi.nodes.layouts.VPage;

/**
 * @author Bruno Salmon
 */
public class HighLevelComponents {

    private static HighLevelComponentsFactory builder;

    public static void register(HighLevelComponentsFactory builder) {
        HighLevelComponents.builder = builder;
    }

    public static HighLevelComponentsFactory getBuilder() {
        if (builder == null)
            register(new HighLevelComponentsFactoryImpl());
        return builder;
    }

    public static VPage createSectionPanel(SectionPanelStyleOptions options) {
        return getBuilder().createSectionPanel(options);
    }

    public static VPage createSectionPanel(SectionPanelStyleOptions options, String iconImageUrl, String translationKey, I18n i18n) {
        return getBuilder().createSectionPanel(options, iconImageUrl, translationKey, i18n);
    }

    public static VPage createSectionPanel(SectionPanelStyleOptions options, GuiNode... headerNodes) {
        return getBuilder().createSectionPanel(options, headerNodes);
    }

}
