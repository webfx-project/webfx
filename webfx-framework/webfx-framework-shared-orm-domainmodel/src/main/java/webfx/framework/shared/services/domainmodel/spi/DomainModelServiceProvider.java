package webfx.framework.shared.services.domainmodel.spi;

import webfx.framework.shared.orm.domainmodel.DomainModel;
import webfx.platform.shared.util.async.Future;

/**
 * @author Bruno Salmon
 */
public interface DomainModelServiceProvider {

    Future<DomainModel> loadDomainModel(Object dataSourceId);

}
