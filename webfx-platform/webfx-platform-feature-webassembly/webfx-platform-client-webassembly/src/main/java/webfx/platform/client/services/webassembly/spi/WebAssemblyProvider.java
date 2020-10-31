package webfx.platform.client.services.webassembly.spi;

import webfx.platform.client.services.webassembly.WebAssemblyImport;
import webfx.platform.client.services.webassembly.WebAssemblyInstance;
import webfx.platform.client.services.webassembly.WebAssemblyModule;
import webfx.platform.shared.util.async.Future;

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
