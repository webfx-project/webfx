package webfx.platform.shared.services.resource.spi;

import webfx.platform.shared.util.async.Future;

/**
 * @author Bruno Salmon
 */
public interface ResourceServiceProvider {

    Future<String> getText(String resourcePath);

}
