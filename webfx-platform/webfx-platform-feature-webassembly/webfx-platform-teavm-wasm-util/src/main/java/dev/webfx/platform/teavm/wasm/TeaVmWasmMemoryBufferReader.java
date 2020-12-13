package dev.webfx.platform.teavm.wasm;


import org.teavm.interop.Address;

import java.nio.charset.StandardCharsets;

/**
 * @author Bruno Salmon
 */
public class TeaVmWasmMemoryBufferReader {

    private Address address;

    public TeaVmWasmMemoryBufferReader(Address address) {
        this.address = address;
    }

    public String readString() {
        if (address.toInt() % 2 != 0)
            address = address.add(1);
        int size = address.getShort();
        address = address.add(2);
        byte[] bytes = new byte[size];
        for (int i = 0; i < size; i++) {
            bytes[i] = address.getByte();
            address = address.add(1);
        }
        return new String(bytes, StandardCharsets.UTF_8);
    }

}
