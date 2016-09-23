package mongoose.activities.shared.highlevelcomponents.impl;

import mongoose.activities.shared.highlevelcomponents.HighLevelComponentsFactory;
import naga.toolkit.spi.Toolkit;
import naga.toolkit.spi.nodes.layouts.VPage;

/**
 * @author Bruno Salmon
 */
public class HighLevelComponentsFactoryImpl implements HighLevelComponentsFactory {

    @Override
    public VPage createSectionPanel() {
        return Toolkit.get().createVPage();
    }
}
