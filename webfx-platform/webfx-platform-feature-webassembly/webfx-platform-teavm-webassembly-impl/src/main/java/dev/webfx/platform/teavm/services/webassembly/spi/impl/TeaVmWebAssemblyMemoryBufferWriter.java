package dev.webfx.platform.teavm.services.webassembly.spi.impl;

import dev.webfx.platform.client.services.webassembly.WebAssemblyMemoryBufferWriter;

import java.nio.charset.StandardCharsets;

/**
 * @author Bruno Salmon
 */
final class TeaVmWebAssemblyMemoryBufferWriter extends TeaVmWebAssemblyMemoryBufferHolder implements WebAssemblyMemoryBufferWriter {

    public TeaVmWebAssemblyMemoryBufferWriter(TeaVmWebAssemblyInstance instance, int offset) {
        super(instance, offset);
    }

    @Override
    public void writeString(String s) {
        byte[] bytes = s.getBytes(StandardCharsets.UTF_8);
        if (offset % 2 != 0)
            offset++;
        int n = bytes.length;
        int16Array.set(offset / 2, (short) n);
        offset += 2;
        for (byte aByte : bytes) int8Array.set(offset++, aByte);
    }
}
