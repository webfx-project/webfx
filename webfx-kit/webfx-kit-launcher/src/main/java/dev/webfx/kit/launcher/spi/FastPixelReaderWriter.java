package dev.webfx.kit.launcher.spi;

import javafx.scene.image.Image;

/**
 * @author Bruno Salmon
 */
public interface FastPixelReaderWriter extends FastPixelReader {

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

    default void setArgb(int opacity, int red, int green, int blue) {
        setOpacity(opacity);
        setRgb(red, green, blue);
    }

    default void setRgb(int red, int green, int blue) {
        setRed(red);
        setGreen(green);
        setBlue(blue);
    }

}
