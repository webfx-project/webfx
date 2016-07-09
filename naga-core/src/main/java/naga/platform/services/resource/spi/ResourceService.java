package naga.platform.services.resource.spi;

import naga.commons.util.async.Future;

/**
 * @author Bruno Salmon
 */
public interface ResourceService {

    Future<String> getText(String resourcePath);

}
