package webfx.platform.services.resource.spi;

import webfx.util.async.Future;

/**
 * @author Bruno Salmon
 */
public interface ResourceServiceProvider {

    Future<String> getText(String resourcePath);

}
