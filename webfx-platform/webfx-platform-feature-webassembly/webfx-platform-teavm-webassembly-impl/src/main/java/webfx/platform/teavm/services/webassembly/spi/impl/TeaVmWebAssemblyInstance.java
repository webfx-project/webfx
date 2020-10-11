package webfx.platform.teavm.services.webassembly.spi.impl;

import org.teavm.jso.JSBody;
import org.teavm.jso.JSObject;
import webfx.platform.client.services.webassembly.WebAssemblyInstance;
import webfx.platform.shared.services.json.Json;

/**
 * @author Bruno Salmon
 */
public class TeaVmWebAssemblyInstance implements WebAssemblyInstance {

    private final JSObject instance;

    public TeaVmWebAssemblyInstance(JSObject instance) {
        this.instance = instance;
    }

    @Override
    public void call(String webAssemblyMethod, Object... args) {
        switch (args.length) {
            case 0: callInstance0(instance, webAssemblyMethod); break;
            case 1: callInstance1(instance, webAssemblyMethod, toJSObject(args[0])); break;
            case 2: callInstance2(instance, webAssemblyMethod, toJSObject(args[0]), toJSObject(args[1])); break;
            case 3: callInstance3(instance, webAssemblyMethod, toJSObject(args[0]), toJSObject(args[1]), toJSObject(args[2])); break;
            case 4: callInstance4(instance, webAssemblyMethod, toJSObject(args[0]), toJSObject(args[1]), toJSObject(args[2]), toJSObject(args[3])); break;
            case 5: callInstance5(instance, webAssemblyMethod, toJSObject(args[0]), toJSObject(args[1]), toJSObject(args[2]), toJSObject(args[3]), toJSObject(args[4])); break;
            case 6: callInstance6(instance, webAssemblyMethod, toJSObject(args[0]), toJSObject(args[1]), toJSObject(args[2]), toJSObject(args[3]), toJSObject(args[4]), toJSObject(args[5])); break;
            case 7: callInstance7(instance, webAssemblyMethod, toJSObject(args[0]), toJSObject(args[1]), toJSObject(args[2]), toJSObject(args[3]), toJSObject(args[4]), toJSObject(args[5]), toJSObject(args[6])); break;
            case 8: callInstance8(instance, webAssemblyMethod, toJSObject(args[0]), toJSObject(args[1]), toJSObject(args[2]), toJSObject(args[3]), toJSObject(args[4]), toJSObject(args[5]), toJSObject(args[6]), toJSObject(args[7])); break;
        }
    }
    
    private static JSObject toJSObject(Object javaObject) {
        return (JSObject) Json.javaToNativeScalar(javaObject);
    }

    @JSBody(params = {"instance", "method"}, script = "instance.exports[method]()")
    private static native void callInstance0(JSObject instance, String webAssemblyMethod);

    @JSBody(params = {"instance", "method", "a1"}, script = "instance.exports[method](a1)")
    private static native void callInstance1(JSObject instance, String webAssemblyMethod, JSObject a1);

    @JSBody(params = {"instance", "method", "a1", "a2"}, script = "instance.exports[method](a1, a2)")
    private static native void callInstance2(JSObject instance, String webAssemblyMethod, JSObject a1, JSObject a2);

    @JSBody(params = {"instance", "method", "a1", "a2", "a3"}, script = "instance.exports[method](a1, a2, a3)")
    private static native void callInstance3(JSObject instance, String webAssemblyMethod, JSObject a1, JSObject a2, JSObject a3);

    @JSBody(params = {"instance", "method", "a1", "a2", "a3", "a4"}, script = "instance.exports[method](a1, a2, a3, a4)")
    private static native void callInstance4(JSObject instance, String webAssemblyMethod, JSObject a1, JSObject a2, JSObject a3, JSObject a4);

    @JSBody(params = {"instance", "method", "a1", "a2", "a3", "a4", "a5"}, script = "instance.exports[method](a1, a2, a3, a4, a5)")
    private static native void callInstance5(JSObject instance, String webAssemblyMethod, JSObject a1, JSObject a2, JSObject a3, JSObject a4, JSObject a5);

    @JSBody(params = {"instance", "method", "a1", "a2", "a3", "a4", "a5", "a6"}, script = "instance.exports[method](a1, a2, a3, a4, a5, a6)")
    private static native void callInstance6(JSObject instance, String webAssemblyMethod, JSObject a1, JSObject a2, JSObject a3, JSObject a4, JSObject a5, JSObject a6);

    @JSBody(params = {"instance", "method", "a1", "a2", "a3", "a4", "a5", "a6", "a7"}, script = "instance.exports[method](a1, a2, a3, a4, a5, a6, a7)")
    private static native void callInstance7(JSObject instance, String webAssemblyMethod, JSObject a1, JSObject a2, JSObject a3, JSObject a4, JSObject a5, JSObject a6, JSObject a7);

    @JSBody(params = {"instance", "method", "a1", "a2", "a3", "a4", "a5", "a6", "a7", "a8"}, script = "instance.exports[method](a1, a2, a3, a4, a5, a6, a7, a8)")
    private static native void callInstance8(JSObject instance, String webAssemblyMethod, JSObject a1, JSObject a2, JSObject a3, JSObject a4, JSObject a5, JSObject a6, JSObject a7, JSObject a8);
}
