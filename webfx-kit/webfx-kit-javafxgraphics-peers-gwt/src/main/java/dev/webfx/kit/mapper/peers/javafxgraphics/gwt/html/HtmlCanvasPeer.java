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
        int width, height;
        if (image != null) {
            width  = (int) image.getWidth();
            if (width == 0)
                width = (int) image.getRequestedWidth();
            height = (int) image.getHeight();
            if (height == 0)
                height = (int) image.getRequestedHeight();
        } else {
            width  = (int) canvas.getWidth();
            height = (int) canvas.getHeight();
            image  = new WritableImage(width , height);
        }

        HTMLCanvasElement canvasToCopy = getCanvasElement();
        setPeerImageData(image, getCanvasImageData(canvasToCopy, width, height));

        return image;
    }

    static void setPeerImageData(Image image, ImageData imageData) {
        image.setPeerImageData(imageData);
        image.setPixelReaderFactory(() -> new ImageDataPixelReader(getPeerImageData(image)));
    }

    static ImageData getPeerImageData(Image image) {
        Object peerImageData = image.getPeerImageData();
        return peerImageData instanceof ImageData ? (ImageData) peerImageData : null;
    }

    public static ImageData getOrCreatePeerImageData(Image image) {
        ImageData imageData = getPeerImageData(image);
        if (imageData == null) {
            HTMLCanvasElement peerCanvas = getOrCreatePeerCanvas(image);
            imageData = getCanvasImageData(peerCanvas);
            setPeerImageData(image, imageData);
        }
        return imageData;
    }

    static void setPeerCanvas(Image image, HTMLCanvasElement canvasElement, boolean dirty) {
        image.setPeerCanvas(canvasElement);
        image.setPeerCanvasDirty(dirty);
    }

    static HTMLCanvasElement getPeerCanvas(Image image) {
        Object peerCanvas = image.getPeerCanvas();
        return peerCanvas instanceof HTMLCanvasElement ? (HTMLCanvasElement) peerCanvas : null;
    }

    static HTMLCanvasElement getOrCreatePeerCanvas(Image image) {
        HTMLCanvasElement peerCanvas = getPeerCanvas(image);
        if (peerCanvas == null) {
            int width = (int) image.getWidth();
            if (width == 0)
                width = (int) image.getRequestedWidth();
            int height = (int) image.getHeight();
            if (height == 0)
                height = (int) image.getRequestedHeight();
            peerCanvas = createCanvasElement(width, height);
            boolean loadedImage = image.getUrl() != null;
            if (loadedImage)
                HtmlGraphicsContext.getCanvasRenderingContext2D(peerCanvas).drawImage(HtmlGraphicsContext.getHTMLImageElement(image), 0, 0);
            else
                setPeerCanvas(image, peerCanvas, true);
        }
        return peerCanvas;
    }

    static HTMLCanvasElement getRenderingCanvas(Image image) {
        HTMLCanvasElement peerCanvas = getOrCreatePeerCanvas(image);
        if (image.isPeerCanvasDirty()) {
            ImageData imageData = getPeerImageData(image);
            if (imageData != null)
                getCanvasContext(peerCanvas).putImageData(imageData, 0, 0);
            image.setPeerCanvasDirty(false);
        }
        return peerCanvas;
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
        return getCanvasImageData(canvasElement, canvasElement.width, canvasElement.height);
    }

    public static ImageData getCanvasImageData(HTMLCanvasElement canvasElement, int width, int height) {
        return getCanvasContext(canvasElement).getImageData(0, 0, width, height);
    }

    static HTMLCanvasElement getImageCanvasElement(Image image) {
        HTMLCanvasElement htmlPeerCanvas = getPeerCanvas(image);
        if (htmlPeerCanvas == null) {
            Object peerCanvas = image.getPeerCanvas();
            if (peerCanvas instanceof HtmlSvgNodePeer)
                peerCanvas = ((HtmlSvgNodePeer<?, ?, ?, ?>) peerCanvas).getElement();
            if (peerCanvas instanceof HTMLCanvasElement)
                htmlPeerCanvas = (HTMLCanvasElement) peerCanvas;
        }
        return htmlPeerCanvas;
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
