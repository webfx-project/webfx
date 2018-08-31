package webfx.platform.services.storage;

import webfx.platform.services.storage.spi.SessionStorageProvider;
import webfx.util.serviceloader.ServiceLoaderHelper;

import java.util.Iterator;

/**
 * @author Bruno Salmon
 */
public class SessionStorage {

    public static SessionStorageProvider getProvider() {
        return ServiceLoaderHelper.loadService(SessionStorageProvider.class);
    }

    public static void setItem(String key, String value) {
        getProvider().setItem(key, value);
    }

    public static String getItem(String key) {
        return getProvider().getItem(key);
    }

    public static void removeItem(String key) {
        getProvider().removeItem(key);
    }

    public static Iterator<String> getKeys() {
        return getProvider().getKeys();
    }

    public static int length() {
        return getProvider().getLength();
    }

    public static void clear() {
        getProvider().clear();
    }

}
