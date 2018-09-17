package webfx.platform.gwt.services.storage;

import com.google.gwt.storage.client.Storage;
import webfx.platforms.core.services.storage.spi.SessionStorageProvider;


/**
 * @author Bruno Salmon
 */
public class GwtSessionStorageProviderImpl extends GwtStorageProviderImpl implements SessionStorageProvider {

    public GwtSessionStorageProviderImpl() {
        super(Storage.getSessionStorageIfSupported());
    }
}
