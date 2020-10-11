package webfx.platform.teavm.services.webassembly.spi.impl;

import org.teavm.jso.JSBody;
import org.teavm.jso.JSFunctor;
import org.teavm.jso.JSObject;
import webfx.platform.client.services.webassembly.Import;
import webfx.platform.client.services.webassembly.WebAssemblyInstance;
import webfx.platform.client.services.webassembly.spi.WebAssemblyProvider;
import webfx.platform.shared.services.json.Json;
import webfx.platform.shared.services.json.WritableJsonObject;
import webfx.platform.shared.util.async.Future;

import java.util.Arrays;

/**
 * @author Bruno Salmon
 */
public class TeaVmWebAssemblyProvider implements WebAssemblyProvider {

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
    public Future<WebAssemblyInstance> load(String webAssemblyUrl, Import... imports) {
        Future<WebAssemblyInstance> future = Future.future();
        WritableJsonObject json = Json.createObject();
        Arrays.stream(imports).forEach(i -> {
            WritableJsonObject mod = (WritableJsonObject) json.getObject(i.getModuleName());
            if (mod == null)
                json.set(i.getModuleName(), mod = Json.createObject());
            //Import.Fn method = i.getMethod();
            //method = TeaVmWebAssemblyProvider::callbackJS;
            BiIntHandler ih = (x, count) -> i.getMethod().handle(x, count);
            setImportFunction((JSObject) mod.getNativeElement(), i.getFunctionName(), ih);
            //mod.setNativeElement(i.getFunctionName(), i.getMethod());
        });
        loadAndInstantiate(webAssemblyUrl, (JSObject) json.getNativeElement(), instance -> future.complete(new TeaVmWebAssemblyInstance(instance)), this::putwchar);
        return future;
    }


    @JSBody(params = {"mod", "fname", "fn"}, script = "mod[fname] = fn")
    private static native void setImportFunction(JSObject mod, String fname, BiIntHandler fn);

    private StringBuffer sb = new StringBuffer();

    private void putwchar(int charCode) {
        if (charCode != 10)
            sb.append(charCodeToString(charCode));
        else {
            System.out.println(sb.toString());
            sb = new StringBuffer();
        }
    }

    @JSBody(params = {"charCode"}, script = "return String.fromCharCode(charCode)")
    private static native String charCodeToString(int charCode);

    @JSBody(params = {"url", "imports", "handler", "putwchar"}, script = "fetch(url)" +
            ".then(response => response.arrayBuffer())" +
            ".then(bytes => WebAssembly.instantiate(bytes, Object.assign(imports, {teavm : {\n" +
            "            currentTimeMillis: function() { return new Date().getTime(); },\n" +
            "            isnan: isNaN,\n" +
            "            teavm_getNaN: function() { return NaN; },\n" +
            "            isinf: function(n) { return !isFinite(n) },\n" +
            "            isfinite: isFinite,\n" +
            "            putwchar: putwchar,\n" +
            "            towlower: function (code) { return String.fromCharCode(code).toLowerCase().charCodeAt(0); },\n" +
            "            towupper: function (code) { return String.fromCharCode(code).toUpperCase().charCodeAt(0); },\n" +
            "            getNativeOffset: function (instant) { return new Date(instant).getTimezoneOffset(); },\n" +
            "            logString: console.log,\n" +
            "            logInt: console.log,\n" +
            "            logOutOfMemory: function() { console.log(\"Out of memory\") }\n" +
            "        }})))" +
            ".then(({ instance }) => handler(instance))")
    private static native void loadAndInstantiate(String url, JSObject imports, JSObjectHandler handler, IntHandler putwchar);

    @JSFunctor
    interface JSObjectHandler extends JSObject {
        void handle(JSObject jso);
    }

    @JSFunctor
    interface IntHandler extends JSObject {
        void handle(int c);
    }

    @JSFunctor
    interface VoidHandler extends JSObject {
        void handle();
    }

    @JSFunctor
    interface BiIntHandler extends JSObject {
        void apply(int x, int count);
    }


}