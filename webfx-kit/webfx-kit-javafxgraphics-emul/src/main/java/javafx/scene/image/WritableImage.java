package javafx.scene.image;

import javafx.scene.paint.Color;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

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
        super(null);
    }

    public PixelWriter getPixelWriter() {
        return new PixelWriter() {
            @Override
            public PixelFormat getPixelFormat() {
                return null;
            }

            @Override
            public void setArgb(int x, int y, int argb) {

            }

            @Override
            public void setColor(int x, int y, Color c) {

            }

            @Override
            public <T extends Buffer> void setPixels(int x, int y, int w, int h, PixelFormat<T> pixelformat, T buffer, int scanlineStride) {

            }

            @Override
            public void setPixels(int x, int y, int w, int h, PixelFormat<ByteBuffer> pixelformat, byte[] buffer, int offset, int scanlineStride) {

            }

            @Override
            public void setPixels(int x, int y, int w, int h, PixelFormat<IntBuffer> pixelformat, int[] buffer, int offset, int scanlineStride) {

            }

            @Override
            public void setPixels(int dstx, int dsty, int w, int h, PixelReader reader, int srcx, int srcy) {

            }
        };
    }
}
