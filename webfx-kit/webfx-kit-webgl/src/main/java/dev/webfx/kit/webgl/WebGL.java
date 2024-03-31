package dev.webfx.kit.webgl;

import dev.webfx.kit.webgl.spi.WebGLProvider;
import dev.webfx.platform.service.SingleServiceProvider;
import javafx.scene.canvas.Canvas;

import java.util.ServiceLoader;

/**
 * @author Bruno Salmon
 */
public final class WebGL {

    private WebGL() {}

    private static WebGLProvider getProvider() {
        return SingleServiceProvider.getProvider(WebGLProvider.class, () -> ServiceLoader.load(WebGLProvider.class), SingleServiceProvider.NotFoundPolicy.RETURN_NULL);
    }

    public static boolean supportsWebGL() {
        WebGLProvider provider = getProvider();
        return provider != null && provider.supportsWebGL();
    }

    public static WebGLRenderingContext getWebGLContext(Canvas canvas) {
        WebGLProvider provider = getProvider();
        return provider == null ? null : provider.getWebGLContext(canvas);
    }

}
