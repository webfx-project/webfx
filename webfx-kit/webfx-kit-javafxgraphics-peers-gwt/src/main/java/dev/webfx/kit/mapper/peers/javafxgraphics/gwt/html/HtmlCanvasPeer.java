package dev.webfx.kit.mapper.peers.javafxgraphics.gwt.html;

import dev.webfx.kit.mapper.peers.javafxgraphics.base.CanvasPeerBase;
import dev.webfx.kit.mapper.peers.javafxgraphics.base.CanvasPeerMixin;
import elemental2.dom.HTMLCanvasElement;
import elemental2.dom.HTMLElement;
import elemental2.dom.ImageData;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.WritableImage;

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

        HTMLCanvasElement canvasElement = getCanvasElement();
        ImageData imageData = ImageDataHelper.captureCanvasImageData(canvasElement, width, height);
        ImageDataHelper.associateImageDataWithImage(imageData, image);

        return image;
    }

}
