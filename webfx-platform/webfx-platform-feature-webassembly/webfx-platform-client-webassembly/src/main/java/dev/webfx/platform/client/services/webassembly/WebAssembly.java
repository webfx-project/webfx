package dev.webfx.platform.client.services.webassembly;

import dev.webfx.platform.client.services.webassembly.spi.WebAssemblyProvider;
import dev.webfx.platform.shared.util.async.Future;
import dev.webfx.platform.shared.util.serviceloader.SingleServiceProvider;

import java.util.ServiceLoader;

/**
 * @author Bruno Salmon
 */
public final class WebAssembly {

    public static WebAssemblyProvider getProvider() {
        return SingleServiceProvider.getProvider(WebAssemblyProvider.class, () -> ServiceLoader.load(WebAssemblyProvider.class), SingleServiceProvider.NotFoundPolicy.RETURN_NULL);
    }

    public static boolean isSupported() {
        WebAssemblyProvider provider = getProvider();
        return provider != null && provider.isSupported();
    }

    public static Future<WebAssemblyModule> loadModule(String webAssemblyUrl) {
        return getProvider().loadModule(webAssemblyUrl);
    }

    public static Future<WebAssemblyInstance> loadAndInstantiate(String webAssemblyUrl, WebAssemblyImport... imports) {
        return getProvider().loadAndInstantiate(webAssemblyUrl, imports);
    }
}
