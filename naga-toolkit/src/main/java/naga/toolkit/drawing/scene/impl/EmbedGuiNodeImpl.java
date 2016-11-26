package naga.toolkit.drawing.scene.impl;

import naga.toolkit.drawing.geom.BaseBounds;
import naga.toolkit.drawing.geom.BoxBounds;
import naga.toolkit.drawing.geom.transform.BaseTransform;
import naga.toolkit.drawing.scene.EmbedGuiNode;
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

    @Override
    public BaseBounds impl_computeGeomBounds(BaseBounds bounds, BaseTransform tx) {
        System.out.println("Warning: EmbedGuiNodeImpl.impl_computeGeomBounds() not implemented");
        return new BoxBounds();
        //throw new UnsupportedOperationException("EmbedGuiNodeImpl.impl_computeGeomBounds() not implemented");
    }

}
