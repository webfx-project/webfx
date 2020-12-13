package dev.webfx.platform.client.services.webassembly.spi;

import dev.webfx.platform.client.services.webassembly.WebAssemblyImport;
import dev.webfx.platform.client.services.webassembly.WebAssemblyInstance;
import dev.webfx.platform.client.services.webassembly.WebAssemblyModule;
import dev.webfx.platform.shared.util.async.Future;

/**
 * @author Bruno Salmon
 */
public interface WebAssemblyProvider {

    boolean isSupported();

    Future<WebAssemblyModule> loadModule(String url);

    default Future<WebAssemblyInstance> loadAndInstantiate(String webAssemblyUrl, WebAssemblyImport... imports) {
        return loadModule(webAssemblyUrl).compose(module -> module.instantiate(imports));
    }

}
