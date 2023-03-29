package javafx.stage;

import javafx.geometry.Rectangle2D;
import dev.webfx.kit.launcher.WebFxKitLauncher;

/**
 * @author Bruno Salmon
 */
public interface Screen {

    Rectangle2D getBounds();

    Rectangle2D getVisualBounds();

    double getOutputScaleX();

    double getOutputScaleY();

    static Screen getPrimary() {
        return WebFxKitLauncher.getProvider().getPrimaryScreen();
    }

    static Screen from(Rectangle2D bounds) {
        return from(bounds, bounds);
    }

    static Screen from(Rectangle2D bounds, Rectangle2D visualBounds) {
        return from(bounds, visualBounds, getPrimary().getOutputScaleX(), getPrimary().getOutputScaleY());
    }

    static Screen from(Rectangle2D bounds, Rectangle2D visualBounds, double outputScaleX, double outputScaleY) {
        return new Screen() {
            @Override
            public Rectangle2D getBounds() {
                return bounds;
            }

            @Override
            public Rectangle2D getVisualBounds() {
                return visualBounds;
            }

            @Override
            public double getOutputScaleX() {
                return outputScaleX;
            }

            @Override
            public double getOutputScaleY() {
                return outputScaleY;
            }
        };
    }
}
