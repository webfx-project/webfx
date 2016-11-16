package naga.toolkit.drawing.shapes.impl;

import naga.toolkit.drawing.shapes.EmbedDrawable;
import naga.toolkit.spi.nodes.GuiNode;

/**
 * @author Bruno Salmon
 */
public class EmbedDrawableImpl extends DrawableImpl implements EmbedDrawable {

    private final GuiNode guiNode;

    public EmbedDrawableImpl(GuiNode guiNode) {
        this.guiNode = guiNode;
    }

    @Override
    public GuiNode getGuiNode() {
        return guiNode;
    }
}
