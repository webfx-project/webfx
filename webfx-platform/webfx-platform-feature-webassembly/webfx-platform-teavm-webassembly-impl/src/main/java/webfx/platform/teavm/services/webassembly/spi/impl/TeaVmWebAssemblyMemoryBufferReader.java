package webfx.platform.teavm.services.webassembly.spi.impl;

import webfx.platform.client.services.webassembly.WebAssemblyMemoryBufferReader;

/**
 * @author Bruno Salmon
 */
final class TeaVmWebAssemblyMemoryBufferReader extends TeaVmWebAssemblyMemoryBufferHolder implements WebAssemblyMemoryBufferReader {

    public TeaVmWebAssemblyMemoryBufferReader(TeaVmWebAssemblyInstance instance, int offset) {
        super(instance, offset);
    }

    @Override
    public byte[] readByteArray(int length) {
        byte[] array = new byte[length];
        for (int i = 0; i < length; i++)
            array[i] = int8Array.get(offset++);
        return array;
    }

    @Override
    public int[] readIntArray(int length) {
        int[] array = new int[length];
        while (offset % 4 != 0)
            offset++;
        int index = offset / 4;
        for (int i = 0; i < length; i++)
            array[i] = int32Array.get(index++);
        offset = index * 4;
        return array;
    }
}
