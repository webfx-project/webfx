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

    private PixelWriter pixelWriter;

    public void setPixelWriter(PixelWriter pixelWriter) {
        this.pixelWriter = pixelWriter;
    }

    public PixelWriter getPixelWriter() {
        if (pixelWriter == null)
            pixelWriter = WebFxKitMapper.getImagePixelWriter(this);
        return pixelWriter;
    }

    @Override
    public Object getPeerImageData() {
        Object peerImageData = super.getPeerImageData();
        if (peerImageData == null && pixelWriter instanceof CanvasPixelWriter) {
            Canvas canvas = ((CanvasPixelWriter) pixelWriter).getCanvas();
            if (canvas != null)
                setPeerImageData(peerImageData = canvas.getOrCreateAndBindNodePeer()); // Will be used by HtmlCanvasPeer.getImageCanvasElement()
        }
        return peerImageData;
    }

}
