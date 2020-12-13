package dev.webfx.platform.client.services.webassembly;

/**
 * @author Bruno Salmon
 */
public interface WebAssemblyInstance {

    WebAssemblyMemoryBufferReader getDataReader(int memoryBufferOffset);

    WebAssemblyMemoryBufferWriter getDataWriter(int memoryBufferOffset);

    Object call(String webAssemblyMethod, Object... arguments);

}
