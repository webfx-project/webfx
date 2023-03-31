package dev.webfx.kit.mapper.peers.javafxgraphics.gwt.html;

import dev.webfx.kit.mapper.peers.javafxgraphics.gwt.shared.HtmlSvgNodePeer;
import dev.webfx.kit.mapper.peers.javafxgraphics.gwt.util.HtmlUtil;
import elemental2.dom.CanvasRenderingContext2D;
import elemental2.dom.HTMLCanvasElement;
import elemental2.dom.ImageData;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

/**
 * @author Bruno Salmon
 */
public final class CanvasElementHelper {

    static HTMLCanvasElement createCanvasElement(int width, int height) {
        HTMLCanvasElement canvasElement =  HtmlUtil.createElement("canvas");
        if (width > 0 && height > 0) {
            canvasElement.width = width;
            canvasElement.height = height;
        }
        return canvasElement;
    }

    static HTMLCanvasElement copyCanvasElement(HTMLCanvasElement canvasSource) {
        return copyCanvasElement(canvasSource, null);
    }

    static HTMLCanvasElement copyCanvasElement(HTMLCanvasElement canvasSource, Paint fill) {
        return copyCanvasElement(canvasSource, canvasSource.width, canvasSource.height, fill);
    }

    static HTMLCanvasElement copyCanvasElement(HTMLCanvasElement canvasSource, int width, int height, Paint fill) {
        HTMLCanvasElement canvasCopy = createCanvasElement(width, height);
        if (width > 0 && height > 0) { // Checking size because drawImage is raising an exception on zero sized canvas
            CanvasRenderingContext2D ctx = Context2DHelper.getCanvasContext2D(canvasCopy);
            if (fill != null) {
                if (fill == Color.TRANSPARENT)
                    ctx.clearRect(0, 0, width, height);
                else {
                    // Wrapping the context with HtmlGraphicsContext to be able to call setFill() with JavaFX fill
                    HtmlGraphicsContext gc = new HtmlGraphicsContext(ctx);
                    gc.save();
                    gc.setFill(fill);
                    gc.fillRect(0, 0, width, height);
                    gc.restore();
                }
            }
            ctx.drawImage(canvasSource, 0, 0, width, height, 0, 0, width ,height);
        }
        return canvasCopy;
    }


    // Utility methods to manage JavaFX images that are actually associated to canvas element for the rendering

    static HTMLCanvasElement getCanvasElementReadyToRenderImage(Image image) {
        HTMLCanvasElement canvasElement = getOrCreateCanvasElementAssociatedWithImage(image);
        if (image.isPeerCanvasDirty()) {
            ImageData imageData = ImageDataHelper.getImageDataAssociatedWithImage(image);
            if (imageData != null)
                Context2DHelper.getCanvasContext2D(canvasElement).putImageData(imageData, 0, 0);
            image.setPeerCanvasDirty(false);
        }
        return canvasElement;
    }

    static HTMLCanvasElement getCanvasElementAssociatedWithImage(Image image) {
        HTMLCanvasElement htmlPeerCanvas = getImagePeerCanvas(image);
        if (htmlPeerCanvas == null && image != null) {
            Object peerCanvas = image.getPeerCanvas();
            if (peerCanvas instanceof HtmlSvgNodePeer)
                peerCanvas = ((HtmlSvgNodePeer<?, ?, ?, ?>) peerCanvas).getElement();
            if (peerCanvas instanceof HTMLCanvasElement)
                htmlPeerCanvas = (HTMLCanvasElement) peerCanvas;
        }
        return htmlPeerCanvas;
    }

    static HTMLCanvasElement getOrCreateCanvasElementAssociatedWithImage(Image image) {
        HTMLCanvasElement peerCanvas = getImagePeerCanvas(image);
        if (peerCanvas == null) {
            double width = image.getWidth();
            if (width == 0)
                width = image.getRequestedWidth();
            double height = image.getHeight();
            if (height == 0)
                height = image.getRequestedHeight();
            peerCanvas = createCanvasElement((int) width, (int) height);
            boolean loadedImage = image.getUrl() != null;
            if (loadedImage)
                Context2DHelper.getCanvasContext2D(peerCanvas).drawImage(HtmlGraphicsContext.getHTMLImageElement(image), 0, 0);
            else
                setImagePeerCanvas(image, peerCanvas, true);
        }
        return peerCanvas;
    }

    private static HTMLCanvasElement getImagePeerCanvas(Image image) {
        if (image == null)
            return null;
        Object peerCanvas = image.getPeerCanvas();
        return peerCanvas instanceof HTMLCanvasElement ? (HTMLCanvasElement) peerCanvas : null;
    }

    private static void setImagePeerCanvas(Image image, HTMLCanvasElement canvasElement, boolean dirty) {
        image.setPeerCanvas(canvasElement);
        image.setPeerCanvasDirty(dirty);
    }

}
