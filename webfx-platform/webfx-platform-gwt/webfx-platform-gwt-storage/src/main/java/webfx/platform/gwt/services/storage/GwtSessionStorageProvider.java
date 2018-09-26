package webfx.platform.gwt.services.storage;

import com.google.gwt.storage.client.Storage;
import webfx.platforms.core.services.storage.spi.SessionStorageProvider;


/**
 * @author Bruno Salmon
 */
public final class GwtSessionStorageProvider extends GwtStorageProvider implements SessionStorageProvider {

    public GwtSessionStorageProvider() {
        super(Storage.getSessionStorageIfSupported());
    }
}
