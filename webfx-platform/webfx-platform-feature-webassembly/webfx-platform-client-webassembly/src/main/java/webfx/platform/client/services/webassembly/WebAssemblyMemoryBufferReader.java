package webfx.platform.client.services.webassembly;

/**
 * @author Bruno Salmon
 */
public interface WebAssemblyMemoryBufferReader extends WebAssemblyMemoryBufferHolder {

    byte[] readByteArray(int length);

    int[] readIntArray(int length);

}
