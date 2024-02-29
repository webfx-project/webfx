package dev.webfx.kit.webgl.spi.impl.gwtj2cl;

import dev.webfx.kit.webgl.WebGLUniformLocation;

/**
 * @author Bruno Salmon
 */
public class GwtJ2clWebGLUniformLocation implements WebGLUniformLocation {

    final elemental2.webgl.WebGLUniformLocation jsWebGLUniformLocation;

    public GwtJ2clWebGLUniformLocation(elemental2.webgl.WebGLUniformLocation jsWebGLUniformLocation) {
        this.jsWebGLUniformLocation = jsWebGLUniformLocation;
    }
}
