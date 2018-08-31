package webfx.platform.services.resource;

import webfx.platform.services.resource.spi.ResourceServiceProvider;
import webfx.util.async.Future;
import webfx.util.serviceloader.ServiceLoaderHelper;

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
