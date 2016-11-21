package naga.toolkit.drawing.shapes;

import naga.toolkit.drawing.shapes.impl.EmbedDrawableImpl;
import naga.toolkit.spi.nodes.GuiNode;

/**
 * @author Bruno Salmon
 */
public interface EmbedDrawable extends Drawable {

    GuiNode getGuiNode();

    static EmbedDrawable create(GuiNode guiNode) {
        return new EmbedDrawableImpl(guiNode);
    }

}
