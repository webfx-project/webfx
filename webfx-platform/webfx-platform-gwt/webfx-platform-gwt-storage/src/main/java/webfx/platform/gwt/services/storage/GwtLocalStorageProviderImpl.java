package webfx.platform.gwt.services.storage;

import com.google.gwt.storage.client.Storage;
import webfx.platforms.core.services.storage.spi.LocalStorageProvider;


/**
 * @author Bruno Salmon
 */
public final class GwtLocalStorageProviderImpl extends GwtStorageProviderImpl implements LocalStorageProvider {

    public GwtLocalStorageProviderImpl() {
        super(Storage.getLocalStorageIfSupported());
    }
}
