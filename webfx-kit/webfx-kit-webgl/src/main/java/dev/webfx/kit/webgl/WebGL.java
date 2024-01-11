package dev.webfx.kit.webgl;

import dev.webfx.platform.util.serviceloader.SingleServiceProvider;
import dev.webfx.kit.webgl.spi.WebGLProvider;
import javafx.scene.Node;

import java.util.ServiceLoader;

/**
 * @author Bruno Salmon
 */
public final class WebGL {

    private WebGL() {}

    private static WebGLProvider getProvider() {
        return SingleServiceProvider.getProvider(WebGLProvider.class, () -> ServiceLoader.load(WebGLProvider.class));
    }

    public static boolean supportsWebGL() {
        WebGLProvider provider = SingleServiceProvider.getProvider(WebGLProvider.class, () -> ServiceLoader.load(WebGLProvider.class), SingleServiceProvider.NotFoundPolicy.RETURN_NULL);
        return provider != null && provider.supportsWebGL();
    }

    public static Node createWebGLNode() {
        return getProvider().createWebGLNode();
    }

    public static Node createWebGLNode(double width, double height) {
        return getProvider().createWebGLNode(width, height);
    }

    public static double getWebGLNodeWidth(Node webglNode) {
        return getProvider().getWebGLNodeWidth(webglNode);
    }

    public static double getWebGLNodeHeight(Node webglNode) {
        return getProvider().getWebGLNodeHeight(webglNode);
    }

    public static WebGLRenderingContext getContext(Node webglNode) {
        return getProvider().getContext(webglNode);
    }

    public static ArrayBuffer createFloat32Array(double... array) {
        return getProvider().createFloat32Array(array);
    }

    public static ArrayBuffer createUint16Array(double... array) {
        return getProvider().createUint16Array(array);
    }

    public static ArrayBuffer createUint8Array(double... array) {
        return getProvider().Uint8Array(array);
    }

}
