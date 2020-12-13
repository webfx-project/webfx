package dev.webfx.platform.client.services.storage.spi;

import java.util.Iterator;

/**
 * @author Bruno Salmon
 */
public interface StorageProvider {

    void setItem(String key, String value);

    String getItem(String key);

    void removeItem(String key);

    Iterator<String> getKeys();

    int getLength();

    void clear();

}
