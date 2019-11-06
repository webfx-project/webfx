package webfx.extras.imagestore;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import webfx.kit.launcher.WebFxKitLauncher;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Bruno Salmon
 */
public final class ImageStore {

    private final static Map<String, Image> imagesCache = new /*Weak*/HashMap<>();

    public static ImageView createImageView(String iconPath) {
        double wh = 0;
        boolean resetToIntrinsicSizeOnceLoaded = false;
        if (iconPath != null) {
            if (resetToIntrinsicSizeOnceLoaded = iconPath.contains("/s16/"))
                wh = 16;
            else if (resetToIntrinsicSizeOnceLoaded = iconPath.contains("/s32/"))
                wh = 32;
        }
        return createImageView(iconPath, wh, wh, resetToIntrinsicSizeOnceLoaded);
    }

    public static ImageView createImageView(String iconPath, double w, double h) {
        return createImageView(iconPath, w, h, false);
    }

    public static ImageView createImageView(String iconPath, double w, double h, boolean resetToIntrinsicSizeOnceLoaded) {
        ImageView imageView = new ImageView();
        boolean loaded = loadImageViewImage(imageView, iconPath, w, h, resetToIntrinsicSizeOnceLoaded);
        if (!loaded) {
            if (w > 0)
                imageView.setFitWidth(w);
            if (h > 0)
                imageView.setFitHeight(h);
        }
        return imageView;
    }

    private static boolean loadImageViewImage(ImageView imageView, String iconPath, double w, double h, boolean resetToIntrinsicSizeOnceLoaded) {
        if (iconPath != null) {
            // Checking the FxKit is ready (especially under JavaFx) because any attempt loading an image before JavaFx is ready results in crash
            if (!WebFxKitLauncher.isReady()) { // If not ready, just postponing the image load once it is ready
                WebFxKitLauncher.onReady(() -> loadImageViewImage(imageView, iconPath, w, h, resetToIntrinsicSizeOnceLoaded));
                return false;
            }
            Image image = getOrCreateImage(iconPath, w, h, resetToIntrinsicSizeOnceLoaded);
            if (image != null) {
                imageView.setImage(image);
                if (resetToIntrinsicSizeOnceLoaded) {
                    if (isImageLoaded(image))
                        return true;
                    runOnImageLoaded(image, () -> {
                        imageView.setFitWidth(0d);
                        imageView.setFitHeight(0d);
                    });
                }
            }
        }
        return false;
    }

    private static boolean isImageLoaded(Image image) {
        return image.getWidth() > 0;
    }

    private static void runOnImageLoaded(Image image, Runnable runnable) {
        image.widthProperty().addListener(new ChangeListener<Number>() { // just diamond <> doesn't compile with GWT for any reason
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                observable.removeListener(this);
                runnable.run();
            }
        });
    }

    public static Image getOrCreateImage(String url) {
        return getOrCreateImage(url, 0, 0);
    }

    public static Image getOrCreateImage(String url, double w, double h, boolean resetToIntrinsicSizeOnceLoaded) {
        if (resetToIntrinsicSizeOnceLoaded) // width & height are not applied to the image in this case
            w = h = 0; // keeping 0 (natural size) instead
        return getOrCreateImage(url, w, h);
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
}
