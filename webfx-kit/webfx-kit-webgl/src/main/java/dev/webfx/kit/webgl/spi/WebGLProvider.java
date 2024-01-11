package dev.webfx.kit.webgl.spi;

import dev.webfx.kit.webgl.ArrayBuffer;
import dev.webfx.kit.webgl.WebGLRenderingContext;
import javafx.scene.Node;

/**
 * @author Bruno Salmon
 */
public interface WebGLProvider {

    boolean supportsWebGL();

    Node createWebGLNode();

    Node createWebGLNode(double width, double height);

    double getWebGLNodeWidth(Node webglNode);

    double getWebGLNodeHeight(Node webglNode);

    WebGLRenderingContext getContext(Node webglNode);

    ArrayBuffer createFloat32Array(double... array);

    ArrayBuffer createUint16Array(double... array);

    ArrayBuffer Uint8Array(double... array);

}
