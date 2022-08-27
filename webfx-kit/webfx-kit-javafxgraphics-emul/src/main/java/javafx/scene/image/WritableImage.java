package javafx.scene.image;

import dev.webfx.kit.mapper.WebFxKitMapper;
import javafx.scene.canvas.Canvas;

/**
 * @author Bruno Salmon
 */
public class WritableImage extends Image {

    public WritableImage(String url) {
        super(url);
    }

    public WritableImage(String url, boolean backgroundLoading) {
        super(url, backgroundLoading);
    }

    public WritableImage(String url, double requestedWidth, double requestedHeight, boolean preserveRatio, boolean smooth, boolean backgroundLoading) {
        super(url, requestedWidth, requestedHeight, preserveRatio, smooth, backgroundLoading);
    }

    public WritableImage(int width, int height) {
        super(null, width, height, false, false, false);
        setWidth(width);
        setHeight(height);
    }

    public WritableImage(PixelReader pixelReader, int width, int height) {
        this (width, height);
        if (pixelReader != null) {
            getPixelWriter();
            // Not performant copy TODO: replace with performant copy with ImageData
            for (int y = 0; y < height; y++)
                for (int x = 0; x < width; x++)
                    pixelWriter.setColor(x, y, pixelReader.getColor(x, y));
        }
    }

    private PixelWriter pixelWriter;

    public void setPixelWriter(PixelWriter pixelWriter) {
        this.pixelWriter = pixelWriter;
    }

    public PixelWriter getPixelWriter() {
        if (pixelWriter == null)
            pixelWriter = WebFxKitMapper.getImagePixelWriter(this);
        return pixelWriter;
    }

    public PixelWriter peekPixelWriter() {
        return pixelWriter;
    }

    @Override
    public Object getPeerCanvas() {
        Object peerCanvas = super.getPeerCanvas();
        if (peerCanvas == null && pixelWriter instanceof CanvasPixelWriter) {
            Canvas canvas = ((CanvasPixelWriter) pixelWriter).getCanvas();
            if (canvas != null)
                setPeerCanvas(peerCanvas = canvas.getOrCreateAndBindNodePeer()); // Will be used by HtmlCanvasPeer.getPeerCanvas()
        }
        return peerCanvas;
    }

}
