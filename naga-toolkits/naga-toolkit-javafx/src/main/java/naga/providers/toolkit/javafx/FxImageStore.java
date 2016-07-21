package naga.providers.toolkit.javafx;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import naga.toolkit.display.Label;

import java.io.InputStream;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * @author Bruno Salmon
 */
public class FxImageStore {

    private static Map<String, Image> images = new WeakHashMap<>();

    public static Image getImage(String url) {
        Image image = images.get(url);
        if (image == null && url != null) {
            try (InputStream is = Thread.currentThread().getContextClassLoader().getResource(url).openStream()) {
                images.put(url, image = new Image(is));
            } catch (Exception e) {
            }
        }
        return image;
    }

    public static ImageView createLabelIconImageView(Label label) {
        return createIconImageView(label == null ? null : label.getIconPath());
    }

    public static ImageView createIconImageView(String iconPath) {
        if (iconPath != null) {
            Image image = getImage(iconPath);
            if (image != null)
                return new ImageView(image);
        }
        return null;
    }
}
