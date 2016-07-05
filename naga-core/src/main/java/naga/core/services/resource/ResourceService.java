package naga.core.services.resource;

import naga.core.util.async.Future;

/**
 * @author Bruno Salmon
 */
public interface ResourceService {

    Future<String> getText(String resourcePath);

}
