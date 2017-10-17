package naga.platform.services.resource.spi;

import naga.util.async.Future;

/**
 * @author Bruno Salmon
 */
public interface ResourceService {

    Future<String> getText(String resourcePath);

}
