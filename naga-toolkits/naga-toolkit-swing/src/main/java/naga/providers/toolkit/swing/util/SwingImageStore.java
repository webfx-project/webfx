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

    public static Image getImage(String url) {
        if (url == null)
            return null;
        Image image = imageCache.get(url);
        if (image == null) {
            URL resource = SwingImageStore.class.getClassLoader().getResource(url);
            imageCache.put(url, image = Toolkit.getDefaultToolkit().getImage(resource));
        }
        return image;
    }

    public static ImageIcon getImageIcon(String url) {
        Image image = getImage(url);
        return image == null ? null : new ImageIcon(image);
    }

    public static Font getFont(String url) {
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