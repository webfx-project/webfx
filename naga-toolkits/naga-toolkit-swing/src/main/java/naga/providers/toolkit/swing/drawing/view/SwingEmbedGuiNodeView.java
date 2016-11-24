package naga.providers.toolkit.swing.drawing.view;

import naga.toolkit.drawing.scene.EmbedGuiNode;
import naga.toolkit.drawing.geom.Point2D;
import naga.toolkit.drawing.spi.view.base.EmbedGuiNodeViewBase;
import naga.toolkit.drawing.spi.view.base.EmbedGuiNodeViewMixin;

import javax.swing.*;
import java.awt.*;

/**
 * @author Bruno Salmon
 */
public class SwingEmbedGuiNodeView extends SwingNodeView<EmbedGuiNode, EmbedGuiNodeViewBase, EmbedGuiNodeViewMixin> {

    public SwingEmbedGuiNodeView() {
        super(new EmbedGuiNodeViewBase());
    }

    public JComponent getEmbedSwingComponent() {
        return getNode().getGuiNode().unwrapToNativeNode();
    }

    @Override
    public void paint(Graphics2D g) {
        JComponent component = getEmbedSwingComponent();
        component.setSize(component.getPreferredSize());
        component.paint(g);
    }

    @Override
    public boolean containsPoint(Point2D point) {
        //System.out.println("x = " + point.getX() + ", y =" + point.getY());
        JComponent component = getEmbedSwingComponent();
        component.setSize(component.getPreferredSize());
        return component.contains((int) point.getX(), (int) point.getY());
    }
}
