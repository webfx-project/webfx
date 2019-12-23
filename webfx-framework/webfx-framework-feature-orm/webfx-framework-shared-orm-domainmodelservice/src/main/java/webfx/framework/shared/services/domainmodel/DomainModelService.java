package webfx.framework.shared.services.domainmodel;

import webfx.framework.shared.orm.domainmodel.DomainModel;
import webfx.framework.shared.services.domainmodel.spi.DomainModelProvider;
import webfx.platform.shared.util.async.Future;
import webfx.platform.shared.util.serviceloader.SingleServiceProvider;

import java.util.ServiceLoader;

/**
 * @author Bruno Salmon
 */
public final class DomainModelService {

    public static DomainModelProvider getProvider() {
        return SingleServiceProvider.getProvider(DomainModelProvider.class, () -> ServiceLoader.load(DomainModelProvider.class));
    }

    public static Future<DomainModel> loadDomainModel(Object dataSourceId) {
        return getProvider().loadDomainModel(dataSourceId);
    }

}
