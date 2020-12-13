package dev.webfx.platform.client.services.storage;

import dev.webfx.platform.client.services.storage.spi.LocalStorageProvider;
import dev.webfx.platform.shared.util.serviceloader.SingleServiceProvider;

import java.util.Iterator;
import java.util.ServiceLoader;

/**
 * @author Bruno Salmon
 */
public final class LocalStorage {

    public static LocalStorageProvider getProvider() {
        return SingleServiceProvider.getProvider(LocalStorageProvider.class, () -> ServiceLoader.load(LocalStorageProvider.class));
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
