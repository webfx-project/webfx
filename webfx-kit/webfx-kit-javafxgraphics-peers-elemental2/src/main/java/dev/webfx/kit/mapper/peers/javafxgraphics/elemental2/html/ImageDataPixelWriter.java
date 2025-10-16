package dev.webfx.kit.mapper.peers.javafxgraphics.elemental2.html;

import elemental2.dom.ImageData;
import javafx.scene.image.AbstractPixelWriter;
import javafx.scene.image.Image;
import javafx.scene.image.PixelFormat;
import javafx.scene.paint.Color;

/**
 * @author Bruno Salmon
 */
public class ImageDataPixelWriter extends AbstractPixelWriter {

    private final ImageData imageData;

    public ImageDataPixelWriter(ImageData imageData) {
        this(null, imageData);
    }

    public ImageDataPixelWriter(Image image, ImageData imageData) {
        super(image);
        this.imageData = imageData;
    }

    public ImageData getImageData() {
        return imageData;
    }

    @Override
    public PixelFormat getPixelFormat() {
        return PixelFormat.getByteBgraInstance();
    }

    @Override
    protected void setArgb(int x, int y, int a, int r, int g, int b) {
        setRgba(x, y, r, g, b, a);
    }

    protected void setRgba(int x, int y, double r, double g, double b, double a) {
        int index = (y * imageData.width + x) * 4;
        imageData.data.setAt(index++, r);
        imageData.data.setAt(index++, g);
        imageData.data.setAt(index++, b);
        imageData.data.setAt(index, a);
        markImageCanvasDirty();
    }
        @Override
    public void setColor(int x, int y, Color c) {
        setRgba(x, y, c.getRed() * 255, c.getGreen() * 255, c.getBlue() * 255, c.getOpacity() * 255);
    }
}
