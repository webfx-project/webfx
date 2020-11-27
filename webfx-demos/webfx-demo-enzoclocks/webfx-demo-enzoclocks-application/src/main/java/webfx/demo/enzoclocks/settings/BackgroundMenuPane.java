package webfx.demo.enzoclocks.settings;

import javafx.scene.Cursor;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.paint.LinearGradient;

/**
 * @author Bruno Salmon
 */
public class BackgroundMenuPane extends ResponsiveGridPane {

    private final static String[] BACKGROUND_GRADIENTS = {
            "to top left, #000046, #1cb5e0", // Visions of Grandeur
            "to bottom right, #753a88, #cc2b5e", // Purple love
            "to bottom right, #eb3349, #f45c43", // Cherry
            "to bottom right, #614385, #516395", // Kashmir
            "to bottom right, #7b4397, #dc2430", // Virgin America
            "to bottom right, #43cea2, #185a9d", // Endless river
            "to bottom right, #ffd89b, #19547b", // Dusk
            "to bottom right, #3a1c71, #d76d77, #ffaf7b", // Relay
            "to bottom right, #c33764, #1d2671", // Celestial
            "to bottom right, #aa076b, #61045f", // Aubergine
            "to bottom right, #0052d4, #4364f7, #6fb1fc", // Bluelago
            "to bottom right, #12c2e9, #c471ed, #f64f59", // JShine
            "to bottom right, #f12711, #f5af19", // Flare
            "to bottom right, #654ea3, #eaafc8", // Ultra Violet
            "to bottom right, #1e9600, #fff200, #ff0000", // Rastafari
            "to bottom right, #00b4db, #0083b0", // Blue Raspberry
            "to bottom right, #fc466b, #3f5efb", // Sublime Vivid
            "to bottom right, #0f0c29, #302b63, #24243e", // Lawrencium
            "to top left, #03001e, #7303c0, #ec38bc, #fdeff9", // Argon
            "to bottom right, #22c1c3, #fdbb2d", // Summer
    };

    public BackgroundMenuPane(Pane root) {
        applyBackgroundGradient(root, 0);
        int n = BACKGROUND_GRADIENTS.length;
        Region[] backgroundThumbnails = new Region[n];
        for (int i = 0; i < n; i++) {
            Region thumbnail = new Region();
            int index = i;
            applyBackgroundGradient(thumbnail, index);
            thumbnail.setCursor(Cursor.HAND);
            thumbnail.setOnMouseClicked(e -> {
                applyBackgroundGradient(root, index);
                root.getChildren().remove(this);
            });
            backgroundThumbnails[i] = thumbnail;
        }
        getChildren().setAll(backgroundThumbnails);
    }

    private static void applyBackgroundGradient(Region background, int index) {
        background.setBackground(new Background(new BackgroundFill(LinearGradient.valueOf(BACKGROUND_GRADIENTS[index]), null, null)));
    }

}
