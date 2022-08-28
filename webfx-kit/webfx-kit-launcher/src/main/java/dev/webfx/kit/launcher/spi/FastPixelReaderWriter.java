package dev.webfx.kit.launcher.spi;

import javafx.scene.image.Image;

/**
 * @author Bruno Salmon
 */
public interface FastPixelReaderWriter {

    Image getImage();

    void goToPixel(int x, int y);

    boolean gotToNextPixel();

    int getRed();

    int getGreen();

    int getBlue();

    int getOpacity();

    void setRed(int red);

    void setGreen(int green);

    void setBlue(int blue);

    void setOpacity(int opacity);

}
