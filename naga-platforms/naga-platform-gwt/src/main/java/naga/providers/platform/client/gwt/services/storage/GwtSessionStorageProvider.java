package naga.providers.platform.client.gwt.services.storage;

import com.google.gwt.storage.client.Storage;
import naga.platform.services.storage.spi.LocalStorageProvider;


/**
 * @author Bruno Salmon
 */
public class GwtSessionStorageProvider extends GwtStorageProvider implements LocalStorageProvider {

    public GwtSessionStorageProvider() {
        super(Storage.getSessionStorageIfSupported());
    }
}
