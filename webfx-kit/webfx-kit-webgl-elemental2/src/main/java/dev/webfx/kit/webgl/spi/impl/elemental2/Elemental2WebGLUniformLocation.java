package dev.webfx.kit.webgl.spi.impl.elemental2;

import dev.webfx.kit.webgl.WebGLUniformLocation;

/**
 * @author Bruno Salmon
 */
public class Elemental2WebGLUniformLocation implements WebGLUniformLocation {

    final elemental2.webgl.WebGLUniformLocation jsWebGLUniformLocation;

    public Elemental2WebGLUniformLocation(elemental2.webgl.WebGLUniformLocation jsWebGLUniformLocation) {
        this.jsWebGLUniformLocation = jsWebGLUniformLocation;
    }
}
