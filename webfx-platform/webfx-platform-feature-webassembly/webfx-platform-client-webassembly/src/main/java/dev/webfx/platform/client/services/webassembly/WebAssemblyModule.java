package dev.webfx.platform.client.services.webassembly;

import dev.webfx.platform.shared.util.async.Future;

/**
 * @author Bruno Salmon
 */
public interface WebAssemblyModule {

    Future<WebAssemblyInstance> instantiate(WebAssemblyImport... imports);

}
