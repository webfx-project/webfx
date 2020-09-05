package webfx.platform.shared.services.resource.spi;

import webfx.platform.shared.util.async.Future;

/**
 * @author Bruno Salmon
 */
public interface ResourceServiceProvider {

    String toUrl(String resourcePath, Class<?> loadingClass);

    Future<String> getText(String resourcePath);

}
