package naga.toolkit.fx.scene.impl;

import naga.toolkit.fx.geom.BaseBounds;
import naga.toolkit.fx.geom.BoxBounds;
import naga.toolkit.fx.geom.transform.BaseTransform;
import naga.toolkit.fx.scene.EmbedGuiNode;
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
