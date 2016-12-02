package naga.toolkit.fx.scene;

import naga.toolkit.fx.scene.impl.EmbedGuiNodeImpl;
import naga.toolkit.spi.nodes.GuiNode;

/**
 * @author Bruno Salmon
 */
public interface EmbedGuiNode extends Node {

    GuiNode getGuiNode();

    static EmbedGuiNode create(GuiNode guiNode) {
        return new EmbedGuiNodeImpl(guiNode);
    }

}
