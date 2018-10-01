package webfx.platforms.core.services.resource.spi;

import webfx.platforms.core.util.async.Future;

/**
 * @author Bruno Salmon
 */
public interface ResourceServiceProvider {

    Future<String> getText(String resourcePath);

}
