package dev.webfx.kit.mapper.peers.javafxgraphics.gwt.html;

import dev.webfx.kit.mapper.peers.javafxgraphics.base.CanvasPeerBase;
import dev.webfx.kit.mapper.peers.javafxgraphics.base.CanvasPeerMixin;
import dev.webfx.kit.mapper.peers.javafxgraphics.gwt.util.HtmlUtil;
import elemental2.dom.CanvasRenderingContext2D;
import elemental2.dom.HTMLCanvasElement;
import elemental2.dom.HTMLElement;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

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

    private HTMLCanvasElement getCanvasElement() {
        return (HTMLCanvasElement) getElement();
    }
    @Override
    public void updateWidth(Number width) {
        HTMLCanvasElement canvasElement = getCanvasElement();
        int intWidth = width.intValue();
        // Preventing erasing canvas if already correctly sized
        if (canvasElement.width != intWidth) // May be already correctly set (ex: Canvas in WritableImage)
            canvasElement.width = intWidth;  // Note: this erases the canvas! (even with identical value)
    }

    @Override
    public void updateHeight(Number height) {
        HTMLCanvasElement canvasElement = getCanvasElement();
        int intHeight = height.intValue();
        // Preventing erasing canvas if already correctly sized
        if (canvasElement.height != intHeight) // May be already correctly set (ex: Canvas in WritableImage)
            canvasElement.height = intHeight;  // Note: this erases the canvas! (even with identical value)
    }

    @Override
    public WritableImage snapshot(SnapshotParameters params, WritableImage image) {
        N canvas = getNode();
        if (image == null)
            image = new WritableImage((int) canvas.getWidth(), (int) canvas.getHeight());

        HTMLCanvasElement canvasElement = getCanvasElement();
        image.setPeerImageData(copyCanvas(canvasElement, (int) image.getWidth(), (int) image.getHeight(), params.getFill()));

        return image;
    }

    static HTMLCanvasElement copyCanvas(HTMLCanvasElement canvasSource) {
        return copyCanvas(canvasSource, null);
    }

    static HTMLCanvasElement copyCanvas(HTMLCanvasElement canvasSource, Paint fill) {
        return copyCanvas(canvasSource, canvasSource.width, canvasSource.height, fill);
    }

    static HTMLCanvasElement copyCanvas(HTMLCanvasElement canvasSource, int width, int height, Paint fill) {
        HTMLCanvasElement canvasCopy = HtmlUtil.createElement("canvas");
        canvasCopy.width = width;
        canvasCopy.height = height;
        if (width > 0 && height > 0) { // Checking size because drawImage is raising an exception on zero sized canvas
            CanvasRenderingContext2D ctx = (CanvasRenderingContext2D) (Object) canvasCopy.getContext("2d");
            if (fill != null) {
                if (fill == Color.TRANSPARENT)
                    ctx.clearRect(0, 0, width, height);
                else {
                    HtmlGraphicsContext graphicsContext = new HtmlGraphicsContext(ctx);
                    graphicsContext.setFill(fill);
                    graphicsContext.fillRect(0, 0, width, height);
                }
            }
            ctx.drawImage(canvasSource, 0, 0, width, height, 0, 0, width ,height);
        }
        return canvasCopy;
    }

}
