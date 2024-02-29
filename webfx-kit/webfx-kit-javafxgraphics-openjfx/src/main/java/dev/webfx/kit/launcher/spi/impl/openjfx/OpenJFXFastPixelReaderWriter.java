package dev.webfx.kit.launcher.spi.impl.openjfx;

import dev.webfx.kit.launcher.spi.FastPixelReaderWriter;
import javafx.scene.image.*;

/**
 * @author Bruno Salmon
 */
public class OpenJFXFastPixelReaderWriter implements FastPixelReaderWriter {

    private final Image image;
    private final PixelReader pixelReader;
    private final PixelWriter pixelWriter;
    private final int width, height;
    private byte[] cache;
    private int x = -1, y, cachePos;
    private boolean cachePosValid;
    private int argb; // Used for reading
    private int newA, newR, newG, newB; // Used for writing

    public OpenJFXFastPixelReaderWriter(Image image) {
        this.image = image;
        width = (int) image.getWidth();
        height = (int) image.getHeight();
        pixelReader = image.getPixelReader();
        pixelWriter = image instanceof WritableImage ? ((WritableImage) image).getPixelWriter() : null;
        cleanPixelChanges();
    }

    @Override
    public Image getImage() {
        return image;
    }

    @Override
    public void goToPixel(int x, int y) {
        if (this.x != -1 && this.y < height)
            applyPixelChanges();
        this.x = x;
        this.y = y;
        invalidateCachePos();
    }

    @Override
    public boolean gotToNextPixel() {
        if (y >= height)
            return false;
        if (x != -1)
            applyPixelChanges();
        invalidateCachePos();
        if (++x >= width) {
            x = 0;
            if (++y >= height)
                return false;
        }
        return true;
    }

    private void invalidateCachePos() {
        cachePosValid = false;
    }

    private int getCachePos() {
        if (!cachePosValid) {
            cachePos = 4 * x + 4 * y * width;
            cachePosValid = true;
        }
        return cachePos;
    }

    private void cleanPixelChanges() {
        argb = -1;
        newA = newR = newG = newB = -1;
    }

    private void applyPixelChanges() {
        if (pixelWriter != null && (newA != -1 || newR != -1 || newG != -1 || newB != -1)) {
            if (newA == -1)
                newA = getOpacity();
            if (newR == -1)
                newR = getRed();
            if (newG == -1)
                newG = getGreen();
            if (newB == -1)
                newB = getBlue();
            if (cache == null) {
                int newArgb =  newA << 24 | newR << 16 | newG << 8 | newB;
                pixelWriter.setArgb(x, y, newArgb);
            } else {
                if (newA == 0)
                    newR = newG = newB = 0;
                cache[getCachePos()] = (byte) newB;
                cache[cachePos +1]   = (byte) newG;
                cache[cachePos +2]   = (byte) newR;
                cache[cachePos +3]   = (byte) newA;
            }
        }
        cleanPixelChanges();
    }

    private int readArgb() {
        if (argb == -1)
            argb = pixelReader.getArgb(x, y);
        return argb;
    }

    @Override
    public int getRed() {
        return newR != -1 ? newR : cache != null ? cache[getCachePos() + 2] : readArgb() >> 16 & 0xff;
    }

    @Override
    public int getGreen() {
        return newG != -1 ? newG : cache != null ? cache[getCachePos() + 1] :  readArgb() >> 8 & 0xff;
    }

    @Override
    public int getBlue() {
        return newB != -1 ? newB : cache != null ? cache[getCachePos()] : readArgb() & 0xff;
    }

    @Override
    public int getOpacity() {
        return newA != -1 ? newA : cache != null ? cache[getCachePos() + 3] : readArgb() >> 24 & 0xff;
    }

    @Override
    public void setRed(int red) {
        newR = red;
    }

    @Override
    public void setGreen(int green) {
        newG = green;
    }

    @Override
    public void setBlue(int blue) {
        newB = blue;
    }

    @Override
    public void setOpacity(int opacity) {
        newA = opacity;
    }

    @Override
    public boolean createCache() {
        if (cache == null && pixelWriter != null)
            cache = new byte[4 * width * height];
        return cache != null;
    }

    @Override
    public void writeCache() {
        if (cache != null)
            pixelWriter.setPixels(0, 0, width, height, PixelFormat.getByteBgraPreInstance(), cache, 0, 4 * width);
    }
}
