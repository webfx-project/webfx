package naga.providers.toolkit.javafx.util;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import naga.toolkit.fxdata.displaydata.Label;

import java.util.Map;
import java.util.WeakHashMap;

/**
 * @author Bruno Salmon
 */
public class FxImageStore {

    private final static Map<String, Image> imagesCache = new WeakHashMap<>();

    private static Image getImage(String url) {
        return getImage(url, 0, 0);
    }

    public static Image getImage(String url, double w, double h) {
        Image image = getImageFromCache(url, w, h);
        if (image == null && url != null)
            synchronized (imagesCache) {
                image = getImageFromCache(url, w, h); // double check in case several threads were waiting
                if (image == null) {
                    long t0 = System.currentTimeMillis();
                    imagesCache.put(url, image = new Image(url, w, h, false, false));
                    long t = System.currentTimeMillis() - t0;
                    System.out.println("Image " + url + " loaded in " + t + "ms");
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
