package dev.webfx.kit.webgl.spi.impl.gwt;

import dev.webfx.kit.webgl.WebGLUniformLocation;

/**
 * @author Bruno Salmon
 */
public class GwtWebGLUniformLocation implements WebGLUniformLocation {

    final elemental2.webgl.WebGLUniformLocation jsWebGLUniformLocation;

    public GwtWebGLUniformLocation(elemental2.webgl.WebGLUniformLocation jsWebGLUniformLocation) {
        this.jsWebGLUniformLocation = jsWebGLUniformLocation;
    }
}
