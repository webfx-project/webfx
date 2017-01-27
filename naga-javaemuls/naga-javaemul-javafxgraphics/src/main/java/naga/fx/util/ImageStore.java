package naga.fx.util;

import emul.javafx.scene.image.Image;
import emul.javafx.scene.image.ImageView;
import naga.fxdata.displaydata.Label;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Bruno Salmon
 */
public class ImageStore {

    private final static Map<String, Image> imagesCache =  new HashMap<>(); // new WeakHashMap<>();

    private static Image getOrCreateImage(String url) {
        return getOrCreateImage(url, 0, 0);
    }

    public static Image getOrCreateImage(String url, double w, double h) {
        Image image = getImageFromCache(url, w, h);
        if (image == null && url != null)
            synchronized (imagesCache) {
                image = getImageFromCache(url, w, h); // double check in case several threads were waiting
                if (image == null) {
                    imagesCache.put(url, image = new Image(url, w, h, false, false, true));
                }
            }
        return image;
    }

    public static Image getImageFromCache(String url, double w, double h) {
        Image image = imagesCache.get(url);
        if (image != null && (w != 0 && image.getWidth() != w || h != 0 && image.getHeight() != h))
            image = null;
        return image;
    }

    public static ImageView createLabelIconImageView(Label label) {
        return createImageView(label == null ? null : label.getIconPath());
    }

    public static ImageView createImageView(String iconPath) {
        if (iconPath != null) {
            Image image = getOrCreateImage(iconPath);
            if (image != null)
                return new ImageView(image);
        }
        return null;
    }
}
