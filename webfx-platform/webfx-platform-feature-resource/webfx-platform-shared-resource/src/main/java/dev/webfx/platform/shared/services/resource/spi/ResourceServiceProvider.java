package dev.webfx.platform.shared.services.resource.spi;

import dev.webfx.platform.shared.util.async.Future;

/**
 * @author Bruno Salmon
 */
public interface ResourceServiceProvider {

    String toUrl(String resourcePath, Class<?> loadingClass);

    Future<String> getText(String resourcePath);

}
