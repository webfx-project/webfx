package naga.toolkit.drawing.shapes.impl;

import naga.toolkit.drawing.shapes.EmbedGuiNode;
import naga.toolkit.spi.nodes.GuiNode;

/**
 * @author Bruno Salmon
 */
public class EmbedGuiNodeImpl extends NodeImpl implements EmbedGuiNode {

    private final GuiNode guiNode;

    public EmbedGuiNodeImpl(GuiNode guiNode) {
        this.guiNode = guiNode;
    }

    @Override
    public GuiNode getGuiNode() {
        return guiNode;
    }
}
