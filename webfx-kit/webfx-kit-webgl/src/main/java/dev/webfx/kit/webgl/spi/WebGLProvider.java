package dev.webfx.kit.webgl.spi;

import dev.webfx.kit.webgl.WebGLRenderingContext;
import javafx.scene.canvas.Canvas;

/**
 * @author Bruno Salmon
 */
public interface WebGLProvider {

    boolean supportsWebGL();

    WebGLRenderingContext getWebGLContext(Canvas canvas);

}
