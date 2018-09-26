package webfx.platform.gwt.services.storage;

import com.google.gwt.storage.client.Storage;
import webfx.platforms.core.services.storage.spi.LocalStorageProvider;


/**
 * @author Bruno Salmon
 */
public final class GwtLocalStorageProvider extends GwtStorageProvider implements LocalStorageProvider {

    public GwtLocalStorageProvider() {
        super(Storage.getLocalStorageIfSupported());
    }
}
