package dev.webfx.kit.webgl.spi.impl.gwtj2cl;

import dev.webfx.kit.webgl.WebGLBuffer;

/**
 * @author Bruno Salmon
 */
public class Elemental2WebGLBuffer implements WebGLBuffer {

    final elemental2.webgl.WebGLBuffer jsWebGLBuffer;

    public Elemental2WebGLBuffer(elemental2.webgl.WebGLBuffer jsWebGLBuffer) {
        this.jsWebGLBuffer = jsWebGLBuffer;
    }
}
