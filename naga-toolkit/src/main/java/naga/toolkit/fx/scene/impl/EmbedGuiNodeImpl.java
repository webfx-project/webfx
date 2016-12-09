package naga.toolkit.fx.scene.impl;

import naga.toolkit.fx.scene.EmbedGuiNode;
import naga.toolkit.fx.scene.layout.impl.RegionImpl;
import naga.toolkit.spi.nodes.GuiNode;

/**
 * @author Bruno Salmon
 */
public class EmbedGuiNodeImpl extends RegionImpl implements EmbedGuiNode {

    private final GuiNode guiNode;

    public EmbedGuiNodeImpl(GuiNode guiNode) {
        this.guiNode = guiNode;
    }

    @Override
    public GuiNode getGuiNode() {
        return guiNode;
    }

    @Override
    protected void createLayoutMeasurable(Object proposedLayoutMeasurable) {
        super.createLayoutMeasurable(guiNode);
    }
    
}
