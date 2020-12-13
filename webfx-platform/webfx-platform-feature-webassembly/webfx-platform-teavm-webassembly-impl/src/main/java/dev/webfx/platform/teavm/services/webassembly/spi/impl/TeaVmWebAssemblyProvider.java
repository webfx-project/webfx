package dev.webfx.platform.teavm.services.webassembly.spi.impl;

import org.teavm.jso.JSBody;
import org.teavm.jso.JSFunctor;
import org.teavm.jso.JSObject;
import dev.webfx.platform.client.services.webassembly.WebAssemblyModule;
import dev.webfx.platform.client.services.webassembly.spi.WebAssemblyProvider;
import dev.webfx.platform.shared.util.async.Future;

/**
 * @author Bruno Salmon
 */
public final class TeaVmWebAssemblyProvider implements WebAssemblyProvider {

    private static Boolean supported;

    @Override
    public boolean isSupported() {
        if (supported == null)
            supported = checkWebAssembly();
        return supported;
    }

    @JSBody(script="{" +
            "    try {\n" +
            "        if (typeof WebAssembly === \"object\"\n" +
            "            && typeof WebAssembly.instantiate === \"function\") {\n" +
            "            const module = new WebAssembly.Module(Uint8Array.of(0x0, 0x61, 0x73, 0x6d, 0x01, 0x00, 0x00, 0x00));\n" +
            "            if (module instanceof WebAssembly.Module)\n" +
            "                return new WebAssembly.Instance(module) instanceof WebAssembly.Instance;\n" +
            "        }\n" +
            "    } catch (e) {\n" +
            "    }\n" +
            "    return false;" +
            "}")
    private static native boolean checkWebAssembly();

    @Override
    public Future<WebAssemblyModule> loadModule(String url) {
        Future<WebAssemblyModule> future = Future.future();
        fetchAndCompileModule(url, jsModule -> future.complete(new TeaVmWebAssemblyModule(jsModule)));
        return future;
    }

    @JSBody(params = {"url", "handler"}, script = "fetch(url)" +
            ".then( function(response) { return WebAssembly.compileStreaming(response) } )" +
            ".then( function(module) { return handler(module) } )")
    private static native void fetchAndCompileModule(String url, JSObjectHandler moduleHandler);

    @JSFunctor
    private interface JSObjectHandler extends JSObject {
        void handle(JSObject jso);
    }
}