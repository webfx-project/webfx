package naga.fx.util;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import naga.fxdata.displaydata.Label;

import java.util.Map;
import java.util.WeakHashMap;

/**
 * @author Bruno Salmon
 */
public class ImageStore {

    private final static Map<String, Image> imagesCache = new WeakHashMap<>();

    public static Image getOrCreateImage(String url) {
        return getOrCreateImage(url, 0, 0);
    }

    public static Image getOrCreateImage(String url, double w, double h) {
        Image image = getImageFromCache(url, w, h);
        if (image == null && url != null)
            synchronized (imagesCache) {
                image = getImageFromCache(url, w, h); // double check in case several threads were waiting
                if (image == null) {
                    try {
                        imagesCache.put(url, image = new Image(url, w, h, false, false, true));
                    } catch (Exception e) {
                        System.out.println("Unable to load image from url " + url);
                    }
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
        double wh = 0;
        if (iconPath != null) {
            if (iconPath.contains("/16/"))
                wh = 16;
            else if (iconPath.contains("/32/"))
                wh = 32;
        }
        return createImageView(iconPath, wh, wh);
    }

    public static ImageView createImageView(String iconPath, double w, double h) {
        ImageView imageView = null;
        if (iconPath != null) {
            Image image = getOrCreateImage(iconPath, w, h);
            if (image != null)
                imageView = new ImageView(image);
        }
        if (imageView == null)
            imageView = new ImageView();
        if (w > 0)
            imageView.setFitWidth(w);
        if (h > 0)
            imageView.setFitHeight(h);
        return imageView;
    }
}
