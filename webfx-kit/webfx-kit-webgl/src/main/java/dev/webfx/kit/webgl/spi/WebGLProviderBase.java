package dev.webfx.kit.webgl.spi;

import javafx.scene.canvas.Canvas;

/**
 * @author Bruno Salmon
 */
public abstract class WebGLProviderBase implements WebGLProvider {

    private final boolean SUPPORTS_WEBGL = getWebGLContext(new Canvas()) != null;

    @Override
    public boolean supportsWebGL() {
        return SUPPORTS_WEBGL;
    }

}
