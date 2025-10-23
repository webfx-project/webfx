package dev.webfx.kit.webgl.spi.impl.elemental2;

import dev.webfx.kit.webgl.WebGLShader;

/**
 * @author Bruno Salmon
 */
public class Elemental2WebGLShader implements WebGLShader {

    final elemental2.webgl.WebGLShader jsWebGLShader;

    public Elemental2WebGLShader(elemental2.webgl.WebGLShader jsWebGLShader) {
        this.jsWebGLShader = jsWebGLShader;
    }
}
