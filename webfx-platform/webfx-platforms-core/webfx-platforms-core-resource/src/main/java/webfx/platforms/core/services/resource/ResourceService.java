package webfx.platforms.core.services.resource;

import webfx.platforms.core.services.resource.spi.ResourceServiceProvider;
import webfx.platforms.core.util.async.Future;
import webfx.platforms.core.util.serviceloader.ServiceLoaderHelper;

/**
 * @author Bruno Salmon
 */
public class ResourceService {

    private static ResourceServiceProvider PROVIDER;

    public static ResourceServiceProvider getProvider() {
        if (PROVIDER == null)
            registerProvider(ServiceLoaderHelper.loadService(ResourceServiceProvider.class));
        return PROVIDER;
    }

    public static void registerProvider(ResourceServiceProvider provider) {
        PROVIDER = provider;
    }

    public static Future<String> getText(String resourcePath) {
        return getProvider().getText(resourcePath);
    }
}
