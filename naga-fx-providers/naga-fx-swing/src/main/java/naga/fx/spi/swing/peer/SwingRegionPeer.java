package naga.fx.spi.swing.peer;

import emul.javafx.geometry.Insets;
import naga.commons.util.collection.Collections;
import emul.javafx.scene.layout.*;
import naga.fx.spi.peer.base.RegionPeerBase;
import naga.fx.spi.peer.base.RegionPeerMixin;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

/**
 * @author Bruno Salmon
 */
public abstract class SwingRegionPeer
        <N extends Region, NB extends RegionPeerBase<N, NB, NM>, NM extends RegionPeerMixin<N, NB, NM>>

        extends SwingNodePeer<N, NB, NM>
        implements RegionPeerMixin<N, NB, NM> {

    private SwingPaintUpdater swingPaintUpdater;
    private SwingStrokeUpdater swingStrokeUpdater;
    private RoundRectangle2D.Double rectangle;

    SwingRegionPeer(NB base) {
        super(base);
    }

    private SwingPaintUpdater getOrCreateSwingPaintUpdater() {
        if (swingPaintUpdater == null)
            swingPaintUpdater = new SwingPaintUpdater();
        return swingPaintUpdater;
    }

    private SwingStrokeUpdater getOrCreateSwingStrokeUpdater() {
        if (swingStrokeUpdater == null)
            swingStrokeUpdater = new SwingStrokeUpdater();
        return swingStrokeUpdater;
    }

    private RoundRectangle2D.Double getOrCreateRectangle() {
        if (rectangle == null)
            rectangle = new RoundRectangle2D.Double(0, 0, 0, 0, 0, 0);
        return rectangle;
    }

    @Override
    public void updateWidth(Double width) {
        updateSize();
    }

    @Override
    public void updateHeight(Double height) {
        updateSize();
    }

    protected void updateSize() {
        N node = getNode();
        updateSize(node.getWidth().intValue(), node.getHeight().intValue());
    }

    protected void updateSize(int width, int height) {
        if (this instanceof SwingEmbedComponentPeer) {
            JComponent component = ((SwingEmbedComponentPeer) this).getSwingComponent();
            component.setSize(width, height);
        }
    }

    @Override
    public void updatePadding(Insets padding) {
    }

    @Override
    public void updateBackground(Background background) {
        BackgroundFill firstFill = background == null ? null : Collections.get(background.getFills(), 0);
        if (firstFill == null)
            swingPaintUpdater = null;
        else
            getOrCreateSwingPaintUpdater().updateFromPaint(firstFill.getFill());
    }

    @Override
    public void updateBorder(Border border) {
        if (border == null)
            swingStrokeUpdater = null;
        else {
            getOrCreateSwingStrokeUpdater().updateFromBorder(border);
            BorderStroke firstStroke = Collections.get(border.getStrokes(), 0);
            CornerRadii radii = firstStroke == null ? null : firstStroke.getRadii();
            if (radii != null)
                getOrCreateRectangle().setRoundRect(0, 0, 0, 0, 2 * radii.getTopLeftHorizontalRadius(), 2 * radii.getTopLeftVerticalRadius());
        }
    }

    @Override
    public void prepareCanvasContext(Graphics2D g) {
        super.prepareCanvasContext(g);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

    }

    @Override
    public void paint(Graphics2D g) {
        if (swingPaintUpdater != null || swingStrokeUpdater != null) {
            double width = getNode().getWidth();
            double height = getNode().getHeight();
            getOrCreateRectangle().setFrame(0, 0, width, height);
            if (swingPaintUpdater != null) {
                swingPaintUpdater.updateProportionalGradient(width, height);
                g.setPaint(swingPaintUpdater.getSwingPaint());
                g.fill(rectangle);
            }
            if (swingStrokeUpdater != null && swingStrokeUpdater.getSwingStroke() != null) {
                g.setStroke(swingStrokeUpdater.getSwingStroke());
                swingStrokeUpdater.updateProportionalGradient(width, height);
                g.setPaint(swingStrokeUpdater.getSwingPaint());
                g.draw(rectangle);
            }
        } else
            rectangle = null;
        if (this instanceof SwingEmbedComponentPeer)
            ((SwingEmbedComponentPeer) this).getSwingComponent().paint(g);
    }
}
