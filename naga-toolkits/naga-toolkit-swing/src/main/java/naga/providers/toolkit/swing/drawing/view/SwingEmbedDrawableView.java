package naga.providers.toolkit.swing.drawing.view;

import naga.toolkit.drawing.shapes.EmbedDrawable;
import naga.toolkit.drawing.shapes.Point2D;
import naga.toolkit.drawing.spi.view.base.EmbedDrawableViewBase;
import naga.toolkit.drawing.spi.view.base.EmbedDrawableViewMixin;

import javax.swing.*;
import java.awt.*;

/**
 * @author Bruno Salmon
 */
public class SwingEmbedDrawableView extends SwingDrawableView<EmbedDrawable, EmbedDrawableViewBase, EmbedDrawableViewMixin> {

    public SwingEmbedDrawableView() {
        super(new EmbedDrawableViewBase());
    }

    public JComponent getEmbedSwingComponent() {
        return getDrawable().getGuiNode().unwrapToNativeNode();
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
