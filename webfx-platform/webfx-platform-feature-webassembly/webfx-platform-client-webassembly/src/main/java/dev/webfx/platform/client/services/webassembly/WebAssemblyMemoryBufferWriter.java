package dev.webfx.platform.client.services.webassembly;

/**
 * @author Bruno Salmon
 */
public interface WebAssemblyMemoryBufferWriter extends WebAssemblyMemoryBufferHolder {

    void writeString(String s);

}
