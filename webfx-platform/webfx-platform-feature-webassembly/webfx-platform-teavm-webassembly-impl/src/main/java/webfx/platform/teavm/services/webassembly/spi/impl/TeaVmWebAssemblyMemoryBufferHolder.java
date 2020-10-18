package webfx.platform.teavm.services.webassembly.spi.impl;

import org.teavm.jso.typedarrays.Int16Array;
import org.teavm.jso.typedarrays.Int32Array;
import org.teavm.jso.typedarrays.Int8Array;
import webfx.platform.client.services.webassembly.WebAssemblyMemoryBufferHolder;

/**
 * @author Bruno Salmon
 */
class TeaVmWebAssemblyMemoryBufferHolder implements WebAssemblyMemoryBufferHolder {

    protected final Int8Array int8Array; // byte
    protected final Int16Array int16Array; // short
    protected final Int32Array int32Array; // int
    protected int offset, initialOffset;

    TeaVmWebAssemblyMemoryBufferHolder(TeaVmWebAssemblyInstance instance, int offset) {
        int8Array = instance.int8Array;
        int16Array = instance.int16Array;
        int32Array = instance.int32Array;
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
