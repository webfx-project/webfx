package javafx.scene.image;

import javafx.scene.paint.Color;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

/**
 * @author Bruno Salmon
 */
public abstract class AbstractPixelWriter implements PixelWriter {

    private final Image image;

    public AbstractPixelWriter() {
        this(null);
    }

    public AbstractPixelWriter(Image image) {
        this.image = image;
    }

    protected void markImageCanvasDirty() {
        if (image != null)
            image.setPeerCanvasDirty(true);
    }

    @Override
    public PixelFormat getPixelFormat() {
        return PixelFormat.getByteBgraInstance();
    }

    @Override
    public void setArgb(int x, int y, int argb) {
        setArgbImpl(x, y, argb);
        markImageCanvasDirty();
    }

    protected void setArgbImpl(int x, int y, int argb) {
        int a = ((argb >> 24) & 0xff);
        int r = ((argb >> 16) & 0xff);
        int g = ((argb >>  8) & 0xff);
        int b = ((argb      ) & 0xff);
        setArgb(x, y, a, r, g, b);
    }

    protected void setArgb(int x, int y, int a, int r, int g, int b) {
        setColor(x, y, Color.rgb(r, g, b, a / 255.0));
    }

    @Override
    public void setColor(int x, int y, Color c) {
        throw new UnsupportedOperationException("setColor() in " + getClass());
    }

    @Override
    public <T extends Buffer> void setPixels(int x, int y, int w, int h, PixelFormat<T> pixelformat, T buffer, int scanlineStride) {
        throw new UnsupportedOperationException("setPixels() in " + getClass());
    }

    @Override
    public void setPixels(int x, int y, int w, int h, PixelFormat<ByteBuffer> pixelformat, byte[] buffer, int offset, int scanlineStride) {
        ByteBuffer buf = ByteBuffer.wrap(buffer, offset, h * scanlineStride);
        for (int y0 = 0; y0 < h; y0++)
            for (int x0 = 0; x0 < w; x0++)
                setArgbImpl(x + x0, y + y0, pixelformat.getArgb(buf, x0, y0, scanlineStride));
        markImageCanvasDirty();
    }

    @Override
    public void setPixels(int x, int y, int w, int h, PixelFormat<IntBuffer> pixelformat, int[] buffer, int offset, int scanlineStride) {
        throw new UnsupportedOperationException("setPixels() in " + getClass());
    }

    @Override
    public void setPixels(int dstx, int dsty, int w, int h, PixelReader reader, int srcx, int srcy) {
        //long t0 = System.currentTimeMillis();
        for (int y = 0; y < h; y++) {
              for (int x = 0; x < w; x++) {
                  setArgb(dstx + x, dsty + y, reader.getArgb(srcx + x, srcy + y));
              }
        }
        //long t1 = System.currentTimeMillis();
        //Console.log(getClass() + " executed non-optimised setPixels() executed in " + (t1 - t0) + "ms");
    }

}
