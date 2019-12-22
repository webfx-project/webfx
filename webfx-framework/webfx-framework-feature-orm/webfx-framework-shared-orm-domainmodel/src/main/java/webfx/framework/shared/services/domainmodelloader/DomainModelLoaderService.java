package webfx.framework.shared.services.domainmodelloader;

import webfx.framework.shared.orm.domainmodel.DomainModel;
import webfx.framework.shared.services.domainmodelloader.spi.DomainModelLoaderProvider;
import webfx.platform.shared.util.async.Future;
import webfx.platform.shared.util.serviceloader.SingleServiceProvider;

import java.util.ServiceLoader;

/**
 * @author Bruno Salmon
 */
public final class DomainModelLoaderService {

    public static DomainModelLoaderProvider getProvider() {
        return SingleServiceProvider.getProvider(DomainModelLoaderProvider.class, () -> ServiceLoader.load(DomainModelLoaderProvider.class));
    }


    public static Future<DomainModel> loadDomainModel(Object dataSourceId) {
        return getProvider().loadDomainModel(dataSourceId);
    }

}
