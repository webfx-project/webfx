package webfx.framework.shared.services.domainmodelloader.spi;

import webfx.framework.shared.orm.domainmodel.DomainModel;
import webfx.platform.shared.util.async.Future;

/**
 * @author Bruno Salmon
 */
public interface DomainModelLoaderProvider {

    Future<DomainModel> loadDomainModel(Object dataSourceId);

}
