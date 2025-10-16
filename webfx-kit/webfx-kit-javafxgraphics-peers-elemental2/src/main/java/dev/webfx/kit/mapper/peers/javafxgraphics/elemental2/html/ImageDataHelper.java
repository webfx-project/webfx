package dev.webfx.kit.mapper.peers.javafxgraphics.elemental2.html;

import elemental2.dom.HTMLCanvasElement;
import elemental2.dom.ImageData;
import javafx.scene.image.Image;

/**
 * @author Bruno Salmon
 */
public class ImageDataHelper {

    public static ImageData captureCanvasImageData(HTMLCanvasElement canvasElement) {
        return captureCanvasImageData(canvasElement, canvasElement.width, canvasElement.height);
    }

    public static ImageData captureCanvasImageData(HTMLCanvasElement canvasElement, int width, int height) {
        return Context2DHelper.getCanvasContext2D(canvasElement).getImageData(0, 0, width, height);
    }

    static ImageData getImageDataAssociatedWithImage(Image image) {
        Object peerImageData = image.getPeerImageData();
        return peerImageData instanceof ImageData ? (ImageData) peerImageData : null;
    }

    public static ImageData getOrCreateImageDataAssociatedWithImage(Image image) {
        ImageData imageData = getImageDataAssociatedWithImage(image);
        if (imageData == null) {
            HTMLCanvasElement peerCanvas = CanvasElementHelper.getOrCreateCanvasElementAssociatedWithImage(image);
            imageData = captureCanvasImageData(peerCanvas);
            associateImageDataWithImage(imageData, image);
        }
        return imageData;
    }

    static void associateImageDataWithImage(ImageData imageData, Image image) {
        image.setPeerImageData(imageData);
        image.setPixelReaderFactory(() -> new ImageDataPixelReader(getImageDataAssociatedWithImage(image)));
    }

}
