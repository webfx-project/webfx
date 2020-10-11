package webfx.platform.client.services.webassembly.spi;

import webfx.platform.client.services.webassembly.Import;
import webfx.platform.client.services.webassembly.WebAssemblyInstance;
import webfx.platform.shared.util.async.Future;

/**
 * @author Bruno Salmon
 */
public interface WebAssemblyProvider {

    boolean isSupported();

    Future<WebAssemblyInstance> load(String webAssemblyUrl, Import... imports);

}
