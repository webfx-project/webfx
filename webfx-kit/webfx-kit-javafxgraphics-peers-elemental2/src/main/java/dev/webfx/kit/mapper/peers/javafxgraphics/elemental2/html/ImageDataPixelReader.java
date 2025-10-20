package dev.webfx.kit.mapper.peers.javafxgraphics.elemental2.html;

import elemental2.core.Uint8ClampedArray;
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
        Uint8ClampedArray data = imageData.data;
        int r = data.getAt(i++).intValue();
        int g = data.getAt(i++).intValue();
        int b = data.getAt(i++).intValue();
        int a = data.getAt(i).intValue();
        return a << 24 | r << 16 | g << 8 | b;
    }

    @Override
    public Color getColor(int x, int y) {
        int i = getIndex(x, y);
        Uint8ClampedArray data = imageData.data;
        int r = data.getAt(i++).intValue();
        int g = data.getAt(i++).intValue();
        int b = data.getAt(i++).intValue();
        int a = data.getAt(i).intValue();
        if (a == 255 && r == 0 && g == 0 && b == 0)
            return Color.BLACK;
        return a == 0 ? Color.TRANSPARENT : a != 255 ? Color.rgb(r, g, b, (double) a / 255) : Color.rgb(r, g, b);
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
