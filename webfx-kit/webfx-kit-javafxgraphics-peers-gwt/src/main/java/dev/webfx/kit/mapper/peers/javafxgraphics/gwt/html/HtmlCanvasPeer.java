package dev.webfx.kit.mapper.peers.javafxgraphics.gwt.html;

import dev.webfx.kit.mapper.peers.javafxgraphics.base.CanvasPeerBase;
import dev.webfx.kit.mapper.peers.javafxgraphics.base.CanvasPeerMixin;
import dev.webfx.kit.mapper.peers.javafxgraphics.gwt.shared.HtmlSvgNodePeer;
import dev.webfx.kit.mapper.peers.javafxgraphics.gwt.util.HtmlUtil;
import elemental2.dom.CanvasRenderingContext2D;
import elemental2.dom.HTMLCanvasElement;
import elemental2.dom.HTMLElement;
import elemental2.dom.ImageData;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;
import javafx.scene.image.PixelWriter;
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
        this((NB) new CanvasPeerBase(), createCanvasElement(-1, -1));
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
        HTMLCanvasElement canvasCopy = copyCanvas(canvasElement, (int) image.getWidth(), (int) image.getHeight(), params.getFill());
        setImageCanvasElement(image, canvasCopy);

        return image;
    }

    static void setImageCanvasElement(Image image, HTMLCanvasElement canvasElement) {
        image.setPeerImageData(canvasElement);
        image.setPixelReaderFactory(() -> new ImageDataPixelReader(getCanvasImageData(canvasElement)));
    }

    public static HTMLCanvasElement createCanvasElement(int width, int height) {
        HTMLCanvasElement canvasElement =  HtmlUtil.createElement("canvas");
        if (width > 0 && height > 0) {
            canvasElement.width = width;
            canvasElement.height = height;
        }
        return canvasElement;
    }

    public static CanvasRenderingContext2D getCanvasContext(HTMLCanvasElement canvasElement) {
        return (CanvasRenderingContext2D) (Object) canvasElement.getContext("2d");
    }

    public static ImageData getCanvasImageData(HTMLCanvasElement canvasElement) {
        return getCanvasContext(canvasElement).getImageData(0, 0, canvasElement.width, canvasElement.height);
    }

    static HTMLCanvasElement getImageCanvasElement(Image image) {
        Object peerImageData = image.getPeerImageData();
        if (peerImageData instanceof HtmlSvgNodePeer)
            peerImageData = ((HtmlSvgNodePeer<?, ?, ?, ?>) peerImageData).getElement();
        if (peerImageData instanceof HTMLCanvasElement)
            return (HTMLCanvasElement) peerImageData;
        if (image instanceof WritableImage) {
            PixelWriter pixelWriter = ((WritableImage) image).peekPixelWriter();
            if (pixelWriter instanceof ImageDataPixelWriter) {
                ImageData imageData = ((ImageDataPixelWriter) pixelWriter).getImageData();
                HTMLCanvasElement canvasElement = createCanvasElement(imageData.width, imageData.height);
                getCanvasContext(canvasElement).putImageData(imageData, 0, 0);
                image.setPeerImageData(canvasElement);
                return canvasElement;
            }
        }
        return null;
    }

    static HTMLCanvasElement copyCanvas(HTMLCanvasElement canvasSource) {
        return copyCanvas(canvasSource, null);
    }

    static HTMLCanvasElement copyCanvas(HTMLCanvasElement canvasSource, Paint fill) {
        return copyCanvas(canvasSource, canvasSource.width, canvasSource.height, fill);
    }

    static HTMLCanvasElement copyCanvas(HTMLCanvasElement canvasSource, int width, int height, Paint fill) {
        HTMLCanvasElement canvasCopy = createCanvasElement(width, height);
        if (width > 0 && height > 0) { // Checking size because drawImage is raising an exception on zero sized canvas
            CanvasRenderingContext2D ctx = getCanvasContext(canvasCopy);
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
