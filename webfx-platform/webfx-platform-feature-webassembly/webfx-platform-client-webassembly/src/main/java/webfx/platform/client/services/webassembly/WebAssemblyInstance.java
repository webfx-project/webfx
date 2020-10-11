package webfx.platform.client.services.webassembly;

/**
 * @author Bruno Salmon
 */
public interface WebAssemblyInstance {

    void call(String webAssemblyMethod, Object... arguments);

}
