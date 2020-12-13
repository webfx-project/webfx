package dev.webfx.platform.teavm.services.webassembly.spi.impl;

import org.teavm.jso.typedarrays.*;
import dev.webfx.platform.client.services.webassembly.WebAssemblyMemoryBufferHolder;

/**
 * @author Bruno Salmon
 */
class TeaVmWebAssemblyMemoryBufferHolder implements WebAssemblyMemoryBufferHolder {

    protected final Int8Array int8Array; // byte
    protected final Int16Array int16Array; // short
    protected final Int32Array int32Array; // int
    protected final Float32Array float32Array; // float
    protected final Float64Array float64Array; // double
    protected int offset, initialOffset;

    TeaVmWebAssemblyMemoryBufferHolder(TeaVmWebAssemblyInstance instance, int offset) {
        int8Array = instance.int8Array;
        int16Array = instance.int16Array;
        int32Array = instance.int32Array;
        float32Array = instance.float32Array;
        float64Array = instance.float64Array;
        setMemoryBufferOffset(offset);
    }

    @Override
    public void setMemoryBufferOffset(int offset) {
        this.offset = initialOffset = offset;
    }

    @Override
    public void resetMemoryBufferOffset() {
        this.offset = initialOffset;
    }
}
