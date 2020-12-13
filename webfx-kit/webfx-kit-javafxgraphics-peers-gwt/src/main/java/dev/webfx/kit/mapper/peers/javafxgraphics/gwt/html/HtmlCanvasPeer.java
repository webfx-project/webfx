package dev.webfx.kit.mapper.peers.javafxgraphics.gwt.html;

import elemental2.dom.HTMLCanvasElement;
import elemental2.dom.HTMLElement;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.WritableImage;
import dev.webfx.kit.mapper.peers.javafxgraphics.gwt.util.HtmlUtil;
import dev.webfx.kit.mapper.peers.javafxgraphics.base.CanvasPeerBase;
import dev.webfx.kit.mapper.peers.javafxgraphics.base.CanvasPeerMixin;

/**
 * @author Bruno Salmon
 */
public final class HtmlCanvasPeer
        <N extends Canvas, NB extends CanvasPeerBase<N, NB, NM>, NM extends CanvasPeerMixin<N, NB, NM>>

        extends HtmlNodePeer<N, NB, NM>
        implements CanvasPeerMixin<N, NB, NM> {

    public HtmlCanvasPeer() {
        this((NB) new CanvasPeerBase(), HtmlUtil.createElement("canvas"));
    }

    public HtmlCanvasPeer(NB base, HTMLElement element) {
        super(base, element);
    }

    @Override
    public void updateWidth(Number width) {
        HTMLCanvasElement canvasElement = (HTMLCanvasElement) getElement();
        int intWidth = width.intValue();
        // Preventing erasing canvas if already correctly sized
        if (canvasElement.width != intWidth) // May be already correctly set (ex: Canvas in WritableImage)
            canvasElement.width = intWidth;  // Note: this erases the canvas! (even with identical value)
    }

    @Override
    public void updateHeight(Number height) {
        HTMLCanvasElement canvasElement = (HTMLCanvasElement) getElement();
        int intHeight = height.intValue();
        // Preventing erasing canvas if already correctly sized
        if (canvasElement.height != intHeight) // May be already correctly set (ex: Canvas in WritableImage)
            canvasElement.height = intHeight;  // Note: this erases the canvas! (even with identical value)
    }

    @Override
    public WritableImage snapshot(SnapshotParameters params, WritableImage image) {
        return new HtmlCanvasImage((HTMLCanvasElement) getElement());
    }
}
