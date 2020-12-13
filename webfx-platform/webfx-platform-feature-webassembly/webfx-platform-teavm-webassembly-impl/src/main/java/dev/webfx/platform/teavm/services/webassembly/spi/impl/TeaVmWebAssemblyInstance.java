package dev.webfx.platform.teavm.services.webassembly.spi.impl;

import org.teavm.jso.JSBody;
import org.teavm.jso.JSObject;
import org.teavm.jso.typedarrays.*;
import dev.webfx.platform.client.services.webassembly.WebAssemblyInstance;
import dev.webfx.platform.client.services.webassembly.WebAssemblyMemoryBufferReader;
import dev.webfx.platform.client.services.webassembly.WebAssemblyMemoryBufferWriter;
import dev.webfx.platform.shared.services.json.Json;

/**
 * @author Bruno Salmon
 */
final class TeaVmWebAssemblyInstance implements WebAssemblyInstance {

    private final JSObject instance;
    final Int8Array int8Array; // byte
    final Int16Array int16Array; // short
    final Int32Array int32Array; // int
    final Float32Array float32Array; // float
    final Float64Array float64Array; // double

    TeaVmWebAssemblyInstance(JSObject instance) {
        this.instance = instance;
        ArrayBuffer memoryBuffer = getMemoryBuffer(getInstanceExportedMemory(instance));
        int8Array = Int8Array.create(memoryBuffer);
        int16Array = Int16Array.create(memoryBuffer);
        int32Array = Int32Array.create(memoryBuffer);
        float32Array = Float32Array.create(memoryBuffer);
        float64Array = Float64Array.create(memoryBuffer);
    }

    @JSBody(params = {"memory"}, script = "return memory.buffer")
    private static native ArrayBuffer getMemoryBuffer(JSObject memory);

    @JSBody(params = {"instance"}, script = "return instance.exports.memory")
    private static native ArrayBuffer getInstanceExportedMemory(JSObject instance);

    @Override
    public WebAssemblyMemoryBufferReader getDataReader(int memoryBufferOffset) {
        return new TeaVmWebAssemblyMemoryBufferReader(this, memoryBufferOffset);
    }

    @Override
    public WebAssemblyMemoryBufferWriter getDataWriter(int memoryBufferOffset) {
        return new TeaVmWebAssemblyMemoryBufferWriter(this, memoryBufferOffset);
    }

    @Override
    public Object call(String webAssemblyMethod, Object... args) {
        return Json.nativeToJavaScalar(callJS(webAssemblyMethod, args));
    }

    public JSObject callJS(String webAssemblyMethod, Object... args) {
        switch (args.length) {
            case 0: return callInstance0(instance, webAssemblyMethod);
            case 1: return callInstance1(instance, webAssemblyMethod, toJSObject(args[0]));
            case 2: return callInstance2(instance, webAssemblyMethod, toJSObject(args[0]), toJSObject(args[1]));
            case 3: return callInstance3(instance, webAssemblyMethod, toJSObject(args[0]), toJSObject(args[1]), toJSObject(args[2]));
            case 4: return callInstance4(instance, webAssemblyMethod, toJSObject(args[0]), toJSObject(args[1]), toJSObject(args[2]), toJSObject(args[3]));
            case 5: return callInstance5(instance, webAssemblyMethod, toJSObject(args[0]), toJSObject(args[1]), toJSObject(args[2]), toJSObject(args[3]), toJSObject(args[4]));
            case 6: return callInstance6(instance, webAssemblyMethod, toJSObject(args[0]), toJSObject(args[1]), toJSObject(args[2]), toJSObject(args[3]), toJSObject(args[4]), toJSObject(args[5]));
            case 7: return callInstance7(instance, webAssemblyMethod, toJSObject(args[0]), toJSObject(args[1]), toJSObject(args[2]), toJSObject(args[3]), toJSObject(args[4]), toJSObject(args[5]), toJSObject(args[6]));
            case 8: return callInstance8(instance, webAssemblyMethod, toJSObject(args[0]), toJSObject(args[1]), toJSObject(args[2]), toJSObject(args[3]), toJSObject(args[4]), toJSObject(args[5]), toJSObject(args[6]), toJSObject(args[7]));
            case 9: return callInstance9(instance, webAssemblyMethod, toJSObject(args[0]), toJSObject(args[1]), toJSObject(args[2]), toJSObject(args[3]), toJSObject(args[4]), toJSObject(args[5]), toJSObject(args[6]), toJSObject(args[7]), toJSObject(args[8]));
        }
        return null;
    }

    private static JSObject toJSObject(Object javaObject) {
        return (JSObject) Json.javaToNativeScalar(javaObject);
    }

    @JSBody(params = {"instance", "method"}, script = "return instance.exports[method]()")
    private static native JSObject callInstance0(JSObject instance, String webAssemblyMethod);

    @JSBody(params = {"instance", "method", "a1"}, script = "return instance.exports[method](a1)")
    private static native JSObject callInstance1(JSObject instance, String webAssemblyMethod, JSObject a1);

    @JSBody(params = {"instance", "method", "a1", "a2"}, script = "return instance.exports[method](a1, a2)")
    private static native JSObject callInstance2(JSObject instance, String webAssemblyMethod, JSObject a1, JSObject a2);

    @JSBody(params = {"instance", "method", "a1", "a2", "a3"}, script = "return instance.exports[method](a1, a2, a3)")
    private static native JSObject callInstance3(JSObject instance, String webAssemblyMethod, JSObject a1, JSObject a2, JSObject a3);

    @JSBody(params = {"instance", "method", "a1", "a2", "a3", "a4"}, script = "return instance.exports[method](a1, a2, a3, a4)")
    private static native JSObject callInstance4(JSObject instance, String webAssemblyMethod, JSObject a1, JSObject a2, JSObject a3, JSObject a4);

    @JSBody(params = {"instance", "method", "a1", "a2", "a3", "a4", "a5"}, script = "return instance.exports[method](a1, a2, a3, a4, a5)")
    private static native JSObject callInstance5(JSObject instance, String webAssemblyMethod, JSObject a1, JSObject a2, JSObject a3, JSObject a4, JSObject a5);

    @JSBody(params = {"instance", "method", "a1", "a2", "a3", "a4", "a5", "a6"}, script = "return instance.exports[method](a1, a2, a3, a4, a5, a6)")
    private static native JSObject callInstance6(JSObject instance, String webAssemblyMethod, JSObject a1, JSObject a2, JSObject a3, JSObject a4, JSObject a5, JSObject a6);

    @JSBody(params = {"instance", "method", "a1", "a2", "a3", "a4", "a5", "a6", "a7"}, script = "return instance.exports[method](a1, a2, a3, a4, a5, a6, a7)")
    private static native JSObject callInstance7(JSObject instance, String webAssemblyMethod, JSObject a1, JSObject a2, JSObject a3, JSObject a4, JSObject a5, JSObject a6, JSObject a7);

    @JSBody(params = {"instance", "method", "a1", "a2", "a3", "a4", "a5", "a6", "a7", "a8"}, script = "return instance.exports[method](a1, a2, a3, a4, a5, a6, a7, a8)")
    private static native JSObject callInstance8(JSObject instance, String webAssemblyMethod, JSObject a1, JSObject a2, JSObject a3, JSObject a4, JSObject a5, JSObject a6, JSObject a7, JSObject a8);

    @JSBody(params = {"instance", "method", "a1", "a2", "a3", "a4", "a5", "a6", "a7", "a8", "a9"}, script = "return instance.exports[method](a1, a2, a3, a4, a5, a6, a7, a8, a9)")
    private static native JSObject callInstance9(JSObject instance, String webAssemblyMethod, JSObject a1, JSObject a2, JSObject a3, JSObject a4, JSObject a5, JSObject a6, JSObject a7, JSObject a8, JSObject a9);
}
