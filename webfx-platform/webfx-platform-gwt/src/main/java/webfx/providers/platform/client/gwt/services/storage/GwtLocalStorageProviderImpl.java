package webfx.providers.platform.client.gwt.services.storage;

import com.google.gwt.storage.client.Storage;
import webfx.platform.services.storage.spi.LocalStorageProvider;


/**
 * @author Bruno Salmon
 */
public class GwtLocalStorageProviderImpl extends GwtStorageProviderImpl implements LocalStorageProvider {

    public GwtLocalStorageProviderImpl() {
        super(Storage.getLocalStorageIfSupported());
    }
}
