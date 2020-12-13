package dev.webfx.platform.gwt.services.storage.spi.impl;

import com.google.gwt.storage.client.Storage;
import dev.webfx.platform.client.services.storage.spi.LocalStorageProvider;


/**
 * @author Bruno Salmon
 */
public final class GwtLocalStorageProvider extends GwtStorageProvider implements LocalStorageProvider {

    public GwtLocalStorageProvider() {
        super(Storage.getLocalStorageIfSupported());
    }
}
