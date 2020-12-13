package dev.webfx.platform.java.services.storage.spi.impl;

import dev.webfx.platform.client.services.storage.spi.StorageProvider;

import java.util.Iterator;
import java.util.Properties;

/**
 * @author Bruno Salmon
 */
class JavaStorageProvider implements StorageProvider {

    protected final Properties properties = new Properties();

    @Override
    public void setItem(String key, String value) {
        properties.setProperty(key, value);
    }

    @Override
    public String getItem(String key) {
        return properties.getProperty(key);
    }

    @Override
    public void removeItem(String key) {
        properties.remove(key);
    }

    @Override
    public Iterator<String> getKeys() {
        return properties.stringPropertyNames().iterator();
    }

    @Override
    public int getLength() {
        return properties.size();
    }

    @Override
    public void clear() {
        properties.clear();
    }
}
