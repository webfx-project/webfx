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
        if (image == null) {
            URL resource = SwingImageStore.class.getClassLoader().getResource(url);
            imageCache.put(url, image = Toolkit.getDefaultToolkit().getImage(resource));
        }
        return image;
    }

    public static Icon getIcon(String url) {
        return getIcon(url, 0, 0);
    }

    public static Icon getIcon(String url, int width, int height) {
        Icon icon = iconCache.get(url);
        if (icon != null && (width == 0 || icon.getIconWidth() == width) && (height == 0 || icon.getIconHeight() == height))
            return icon;
        if (url != null && url.endsWith(".svg"))
            try (InputStream is = SwingImageStore.class.getClassLoader().getResourceAsStream(url)) {
                iconCache.put(url, icon = new BatikSvgIcon(is, width, height));
                return icon;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        Image image = getImage(url);
        return image == null ? null : new ImageIcon(image);
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