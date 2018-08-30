package naga.providers.platform.client.gwt.services.storage;

import com.google.gwt.storage.client.Storage;
import naga.platform.services.storage.spi.LocalStorageProvider;


/**
 * @author Bruno Salmon
 */
public class GwtSessionStorageProviderImpl extends GwtStorageProviderImpl implements LocalStorageProvider {

    public GwtSessionStorageProviderImpl() {
        super(Storage.getSessionStorageIfSupported());
    }
}
