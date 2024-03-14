package dev.webfx.kit.webgl.spi.impl.gwtj2cl;

import dev.webfx.kit.webgl.WebGLProgram;

/**
 * @author Bruno Salmon
 */
public class GwtJ2clWebGLProgram implements WebGLProgram {

    final elemental2.webgl.WebGLProgram jsWebGLProgram;

    public GwtJ2clWebGLProgram(elemental2.webgl.WebGLProgram jsWebGLProgram) {
        this.jsWebGLProgram = jsWebGLProgram;
    }
}
