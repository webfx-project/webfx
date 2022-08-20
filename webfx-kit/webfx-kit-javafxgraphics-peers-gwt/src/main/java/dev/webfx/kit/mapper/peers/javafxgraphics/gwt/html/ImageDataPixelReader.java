package dev.webfx.kit.mapper.peers.javafxgraphics.gwt.html;

import elemental2.dom.ImageData;
import javafx.scene.image.PixelFormat;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritablePixelFormat;
import javafx.scene.paint.Color;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

/**
 * @author Bruno Salmon
 */
public class ImageDataPixelReader implements PixelReader {

    private final ImageData imageData;

    public ImageDataPixelReader(ImageData imageData) {
        this.imageData = imageData;
    }

    @Override
    public PixelFormat getPixelFormat() {
        return PixelFormat.getByteBgraInstance();
    }

    private int getIndex(int x, int y) {
        return (y * imageData.width + x) * 4;
    }

    @Override
    public int getArgb(int x, int y) {
        int i = getIndex(x, y);
        int r = imageData.data.getAt(i++).intValue();
        int g = imageData.data.getAt(i++).intValue();
        int b = imageData.data.getAt(i++).intValue();
        int a = imageData.data.getAt(i).intValue();
        return a << 24 | r << 16 | g << 8 | b;
    }

    @Override
    public Color getColor(int x, int y) {
        return null;
    }

    @Override
    public <T extends Buffer> void getPixels(int x, int y, int w, int h, WritablePixelFormat<T> pixelformat, T buffer, int scanlineStride) {

    }

    @Override
    public void getPixels(int x, int y, int w, int h, WritablePixelFormat<ByteBuffer> pixelformat, byte[] buffer, int offset, int scanlineStride) {
    }

    @Override
    public void getPixels(int x, int y, int w, int h, WritablePixelFormat<IntBuffer> pixelformat, int[] buffer, int offset, int scanlineStride) {

    }
}
