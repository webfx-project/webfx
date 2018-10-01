package webfx.platform.client.services.storage.spi.impl.gwt;

import com.google.gwt.storage.client.Storage;
import webfx.platform.client.services.storage.spi.LocalStorageProvider;


/**
 * @author Bruno Salmon
 */
public final class GwtLocalStorageProvider extends GwtStorageProvider implements LocalStorageProvider {

    public GwtLocalStorageProvider() {
        super(Storage.getLocalStorageIfSupported());
    }
}
