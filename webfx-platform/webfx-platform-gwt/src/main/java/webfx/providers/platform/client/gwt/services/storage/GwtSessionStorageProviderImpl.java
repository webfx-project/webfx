package webfx.providers.platform.client.gwt.services.storage;

import com.google.gwt.storage.client.Storage;
import webfx.platform.services.storage.spi.LocalStorageProvider;


/**
 * @author Bruno Salmon
 */
public class GwtSessionStorageProviderImpl extends GwtStorageProviderImpl implements LocalStorageProvider {

    public GwtSessionStorageProviderImpl() {
        super(Storage.getSessionStorageIfSupported());
    }
}
