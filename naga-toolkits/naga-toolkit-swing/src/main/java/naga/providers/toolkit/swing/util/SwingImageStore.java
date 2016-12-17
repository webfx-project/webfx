package naga.providers.toolkit.swing.util;


import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * @author Bruno Salmon
 */
public final class SwingImageStore {

    private static final Map<String, Image> imageCache = new WeakHashMap<>();
    private static final Map<String, Icon> iconCache = new WeakHashMap<>();

    private static Image getImage(String url) {
        if (url == null)
            return null;
        Image image = imageCache.get(url);
        if (image == null)
            synchronized (imageCache) {
                image = imageCache.get(url);
                if (image == null) {
                    long t0 = System.currentTimeMillis();
                    URL resource = SwingImageStore.class.getClassLoader().getResource(url);
                    imageCache.put(url, image = Toolkit.getDefaultToolkit().getImage(resource));
                    long t = System.currentTimeMillis() - t0;
                    System.out.println("Image " + url + " loaded in " + t + "ms");
                }
            }
        return image;
    }

    public static Icon getIcon(String url, int width, int height) {
        Icon icon = getIconFromCache(url, width, height);
        if (icon != null)
            return icon;
        if (url != null && url.endsWith(".svg"))
            synchronized (iconCache) {
                icon = getIconFromCache(url, width, height);
                if (icon == null) {
                    long t0 = System.currentTimeMillis();
                    try (InputStream is = SwingImageStore.class.getClassLoader().getResourceAsStream(url)) {
                        iconCache.put(url, icon = new BatikSvgIcon(is, width, height));
                        long t = System.currentTimeMillis() - t0;
                        System.out.println("Svg image " + url + " loaded in " + t + "ms");
                        return icon;
                    } catch (Exception e) {
                        e.printStackTrace();
                        return null;
                    }
                }
            }
        Image image = getImage(url);
        if (image != null)
            iconCache.put(url, icon = new ImageIcon(image));
        return icon;
    }

    public static Icon getIconFromCache(String url, int w, int h) {
        Icon icon = iconCache.get(url);
        if (icon != null && (w != 0 && icon.getIconWidth() != w && icon.getIconWidth() > 0 || h != 0 && icon.getIconHeight() != h && icon.getIconHeight() > 0))
            icon = null;
        return icon;
    }

    static Font getFont(String url) {
        InputStream is = SwingImageStore.class.getResourceAsStream(url);
        Font font = null;
        try {
            font = Font.createFont(Font.TRUETYPE_FONT, is);
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
        }
        return font;
    }
}