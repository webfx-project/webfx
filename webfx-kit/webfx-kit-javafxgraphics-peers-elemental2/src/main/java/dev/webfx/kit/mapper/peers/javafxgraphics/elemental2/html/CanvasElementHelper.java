package dev.webfx.kit.mapper.peers.javafxgraphics.elemental2.html;

import dev.webfx.kit.launcher.WebFxKitLauncher;
import dev.webfx.kit.mapper.peers.javafxgraphics.elemental2.shared.HtmlSvgNodePeer;
import dev.webfx.kit.mapper.peers.javafxgraphics.elemental2.util.HtmlUtil;
import elemental2.dom.CSSProperties;
import elemental2.dom.CanvasRenderingContext2D;
import elemental2.dom.HTMLCanvasElement;
import elemental2.dom.ImageData;
import javafx.scene.canvas.Canvas;
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

    // Utility methods to resize a Canvas element

    public static void resizeCanvasElement(HTMLCanvasElement canvasElement, Canvas canvas) {
        double fxCanvasWidth = canvas.getWidth(), fxCanvasHeight = canvas.getHeight();
        // While HTML canvas and JavaFX canvas have an identical size in low-res screens, they differ in HDPI screens
        // because JavaFX automatically apply the pixel conversion, while HTML doesn't.
        double pixelDensity = WebFxKitLauncher.getCanvasPixelDensity(canvas);
        int htmlWidth = (int) (fxCanvasWidth * pixelDensity); // So we apply the density factor to get the hi-res number of pixels.
        int htmlHeight = (int) (fxCanvasHeight * pixelDensity);
        // Note: the JavaFX canvas size might be 0 initially, but we set a minimal size of 1px for the HTML canvas, the
        // reason is that transforms applied on zero-sized canvas are ignored on Chromium browsers (for example applying
        // the pixelDensity scale on a zero-sized canvas doesn't change the canvas transform), which would make our
        // canvas state snapshot technique below fail.
        if (htmlWidth == 0)
            htmlWidth = 1;
        if (htmlHeight == 0)
            htmlHeight = 1;
        // It's very important to prevent changing the canvas size when not necessary, because resetting an HTML canvas
        // size has these 2 serious consequences (even with identical value):
        // 1) the canvas is erased
        // 2) the context state is reset (including transforms, such as the initial pixel density on HDPI screens)
        boolean htmlSizeHasChanged = canvasElement.width != htmlWidth || canvasElement.height != htmlHeight;
        if (htmlSizeHasChanged) {
            // Getting the 2D context but only if already created (we don't want to initialize a 2D context if the
            // canvas will finally be used for WebGL in the application code - because the context type (2D or WebGL)
            // can't be changed once initialized). The reason for getting the context is to eventually create a context
            // snapshot (but there is no need if the 2D context was not initialized.
            CanvasRenderingContext2D ctx = canvas.theContext == null ? null : Context2DHelper.getCanvasContext2D(canvasElement);
            // We don't want to lose the context state when resizing the canvas, so we take a snapshot of it before
            // resizing, so we can restore it after that.
            Context2DStateSnapshot ctxStateSnapshot = ctx == null ? null : new Context2DStateSnapshot(ctx);
            // Now we can change the canvas size, as we are prepared
            canvasElement.width = htmlWidth;  // => erases canvas & reset context sate
            canvasElement.height = htmlHeight; // => erases canvas & reset context sate
            // We restore the context state that we have stored in the snapshot (this includes the initial pixelDensity scale)
            if (ctxStateSnapshot != null)
                ctxStateSnapshot.reapply();
            // On HDPI screens, we must also set the CSS size, otherwise the CSS size will be taken from the canvas
            // size by default, which is not what we want because the CSS size is expressed in low-res and not in HDPI
            // pixels like the canvas size, so this would make the canvas appear much too big on the screen.
            if (pixelDensity != 1) { // Scaling down the canvas size with CSS size on HDPI screens
                canvasElement.style.width = CSSProperties.WidthUnionType.of(fxCanvasWidth + "px");
                canvasElement.style.height = CSSProperties.HeightUnionType.of(fxCanvasHeight + "px");
            }
        }
    }
}
