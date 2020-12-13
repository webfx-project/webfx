package dev.webfx.platform.teavm.services.webassembly.spi.impl;

import dev.webfx.platform.client.services.webassembly.WebAssemblyMemoryBufferReader;

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

    @Override
    public float[] readFloatArray(int length) {
        float[] array = new float[length];
        while (offset % 4 != 0)
            offset++;
        int index = offset / 4;
        for (int i = 0; i < length; i++)
            array[i] = float32Array.get(index++);
        offset = index * 4;
        return array;
    }

    @Override
    public double[] readDoubleArray(int length) {
        double[] array = new double[length];
        while (offset % 8 != 0)
            offset++;
        int index = offset / 8;
        for (int i = 0; i < length; i++)
            array[i] = float64Array.get(index++);
        offset = index * 8;
        System.out.println("array[0] = " + array[0]);
        return array;
    }
}
