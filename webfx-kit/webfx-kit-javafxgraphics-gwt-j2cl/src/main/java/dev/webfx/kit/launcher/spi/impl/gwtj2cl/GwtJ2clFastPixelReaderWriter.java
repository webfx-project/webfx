package dev.webfx.kit.launcher.spi.impl.gwtj2cl;

import dev.webfx.kit.launcher.spi.FastPixelReaderWriter;
import dev.webfx.kit.mapper.peers.javafxgraphics.gwtj2cl.html.ImageDataHelper;
import elemental2.dom.ImageData;
import javafx.scene.image.Image;

/**
 * @author Bruno Salmon
 */
public final class GwtJ2clFastPixelReaderWriter implements FastPixelReaderWriter {

    private final Image image;
    private final ImageData imageData;
    private final int maxIndex;
    private int index = -4;

    public GwtJ2clFastPixelReaderWriter(Image image) {
        this.image = image;
        imageData = ImageDataHelper.getOrCreateImageDataAssociatedWithImage(image);
        maxIndex = imageData.data.length - 4;
        image.setPeerCanvasDirty(true);
    }

    @Override
    public Image getImage() {
        return image;
    }

    @Override
    public void goToPixel(int x, int y) {
        index = 4 * (y * imageData.width + x);
    }

    @Override
    public boolean gotToNextPixel() {
        if (index >= maxIndex)
            return false;
        index += 4;
        return true;
    }

    @Override
    public int getRed() {
        return imageData.data.getAt(index).intValue();
    }

    @Override
    public int getGreen() {
        return imageData.data.getAt(index + 1).intValue();
    }

    @Override
    public int getBlue() {
        return imageData.data.getAt(index + 2).intValue();
    }

    @Override
    public int getOpacity() {
        return imageData.data.getAt(index + 3).intValue();
    }

    @Override
    public void setRed(int red) {
        imageData.data.setAt(index, (double) red);
        image.setPeerCanvasDirty(true);
    }

    @Override
    public void setGreen(int green) {
        imageData.data.setAt(index + 1, (double) green);
        image.setPeerCanvasDirty(true);
    }

    @Override
    public void setBlue(int blue) {
        imageData.data.setAt(index + 2, (double) blue);
        image.setPeerCanvasDirty(true);
    }

    @Override
    public void setOpacity(int opacity) {
        imageData.data.setAt(index + 3, (double) opacity);
        image.setPeerCanvasDirty(true);
    }
}
