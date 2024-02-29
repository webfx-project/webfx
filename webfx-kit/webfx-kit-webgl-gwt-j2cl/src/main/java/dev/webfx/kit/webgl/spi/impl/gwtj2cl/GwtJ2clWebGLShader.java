package dev.webfx.kit.webgl.spi.impl.gwtj2cl;

import dev.webfx.kit.webgl.WebGLShader;

/**
 * @author Bruno Salmon
 */
public class GwtJ2clWebGLShader implements WebGLShader {

    final elemental2.webgl.WebGLShader jsWebGLShader;

    public GwtJ2clWebGLShader(elemental2.webgl.WebGLShader jsWebGLShader) {
        this.jsWebGLShader = jsWebGLShader;
    }
}
