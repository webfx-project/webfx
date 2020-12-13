package dev.webfx.platform.gwt.services.storage.spi.impl;

import com.google.gwt.storage.client.Storage;
import dev.webfx.platform.client.services.storage.spi.SessionStorageProvider;


/**
 * @author Bruno Salmon
 */
public final class GwtSessionStorageProvider extends GwtStorageProvider implements SessionStorageProvider {

    public GwtSessionStorageProvider() {
        super(Storage.getSessionStorageIfSupported());
    }
}
