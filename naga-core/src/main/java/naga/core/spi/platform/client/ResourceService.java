package naga.core.spi.platform.client;

import naga.core.util.async.Future;

/**
 * @author Bruno Salmon
 */
public interface ResourceService {

    Future<String> getText(String resourcePath);

}
