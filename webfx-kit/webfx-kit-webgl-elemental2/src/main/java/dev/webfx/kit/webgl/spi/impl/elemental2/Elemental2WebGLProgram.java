package dev.webfx.kit.webgl.spi.impl.elemental2;

import dev.webfx.kit.webgl.WebGLProgram;

/**
 * @author Bruno Salmon
 */
public class Elemental2WebGLProgram implements WebGLProgram {

    final elemental2.webgl.WebGLProgram jsWebGLProgram;

    public Elemental2WebGLProgram(elemental2.webgl.WebGLProgram jsWebGLProgram) {
        this.jsWebGLProgram = jsWebGLProgram;
    }
}
