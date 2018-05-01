package naga.platform.services.storage;

import naga.platform.services.storage.spi.LocalStorageProvider;
import naga.util.serviceloader.ServiceLoaderHelper;

import java.util.Iterator;

/**
 * @author Bruno Salmon
 */
public class LocalStorage {

    public static LocalStorageProvider getProvider() {
        return ServiceLoaderHelper.loadService(LocalStorageProvider.class);
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
