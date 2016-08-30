package naga.providers.toolkit.swing.util;


import javax.swing.*;
import java.net.URL;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * @author Bruno Salmon
 */
public final class SwingImageStore {
    private static final Map<String, URL> resourceCache = new WeakHashMap<>();
    private static final Map<String, Icon> imageIconCache = new WeakHashMap<>();

    public static Icon getIcon(String iconUrl) {
        if (iconUrl == null)
            return null;
        Icon icon = imageIconCache.get(iconUrl);
        if (icon == null) {
            URL url = getResource(iconUrl);
            if (url != null)
               icon = new ImageIcon(url);
            imageIconCache.put(iconUrl, icon);
        }
        return icon;
    }

    static URL getResource(String name) {
        if (name == null)
            return null;
        URL url = resourceCache.get(name);
        if (url == null) {
            url = SwingImageStore.class.getClassLoader().getResource(name);
            resourceCache.put(name, url);
        }
        return url;
    }
}
