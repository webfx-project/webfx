package dev.webfx.kit.mapper.peers.javafxgraphics.elemental2.html;

import dev.webfx.kit.launcher.WebFxKitLauncher;
import dev.webfx.kit.mapper.peers.javafxgraphics.base.CanvasPeerBase;
import dev.webfx.kit.mapper.peers.javafxgraphics.base.CanvasPeerMixin;
import elemental2.dom.HTMLCanvasElement;
import elemental2.dom.HTMLElement;
import elemental2.dom.ImageData;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.WritableImage;
import javafx.scene.transform.Scale;
import javafx.scene.transform.Transform;

/**
 * @author Bruno Salmon
 */
public final class HtmlCanvasPeer
        <N extends Canvas, NB extends CanvasPeerBase<N, NB, NM>, NM extends CanvasPeerMixin<N, NB, NM>>

        extends HtmlNodePeer<N, NB, NM>
        implements CanvasPeerMixin<N, NB, NM> {

    public HtmlCanvasPeer() {
        this((NB) new CanvasPeerBase(), CanvasElementHelper.createCanvasElement(-1, -1));
    }

    public HtmlCanvasPeer(NB base, HTMLElement element) {
        super(base, element);
    }

    private HTMLCanvasElement getCanvasElement() {
        return (HTMLCanvasElement) getElement();
    }
    @Override
    public void updateWidth(Number width) {
        // Note: probably already updated by HtmlGraphicsContext
        CanvasElementHelper.resizeCanvasElement(getCanvasElement(), getNode());
    }

    @Override
    public void updateHeight(Number height) {
        // Note: probably already updated by HtmlGraphicsContext
        CanvasElementHelper.resizeCanvasElement(getCanvasElement(), getNode());
    }

    @Override
    public WritableImage snapshot(SnapshotParameters params, WritableImage image) {
        double scaleX = 1, scaleY = 1;
        if (params != null) {
            Transform transform = params.getTransform();
            if (transform instanceof Scale) {
                Scale scale = (Scale) transform;
                scaleX = scale.getX();
                scaleY = scale.getY();
            }
        }
        N canvas = getNode();
        int width, height;
        if (image != null) {
            width  = (int) image.getWidth();
            if (width == 0)
                width = (int) image.getRequestedWidth();
            height = (int) image.getHeight();
            if (height == 0)
                height = (int) image.getRequestedHeight();
            // The same writable image instance may be used several times to create several canvas snapshots. So it's
            // important to clear the canvas cache of that image (which may contain a previous snapshot set by
            // CanvasElementHelper using gc.putImageData()). This will ensure that any new call to gc.drawImage() will
            // render the new version of this image (= this snapshot).
            image.setPeerCanvas(null);
        } else {
            width  = (int) (canvas.getWidth() * scaleX);
            height = (int) (canvas.getHeight() * scaleY);
            image  = new WritableImage(width , height);
        }

        HTMLCanvasElement canvasToCopy = getCanvasElement();
        double pixelDensity = WebFxKitLauncher.getCanvasPixelDensity(canvas);
        // Making a rescaled copy of the canvas if necessary before capturing the image
        if (scaleX != pixelDensity || scaleY != pixelDensity) {
            HTMLCanvasElement c = CanvasElementHelper.createCanvasElement(width, height);
            // Note: wrong Elemental2 signature. Correct signature = drawImage(image, sx, sy, sw, sh, dx, dy, dw, dh)
            Context2DHelper.getCanvasContext2D(c).drawImage(canvasToCopy, 0, 0, width * pixelDensity, height * pixelDensity, 0, 0, width, height);
            canvasToCopy = c;
        }
        ImageData imageData = ImageDataHelper.captureCanvasImageData(canvasToCopy, width, height);
        ImageDataHelper.associateImageDataWithImage(imageData, image);

        return image;
    }

}
