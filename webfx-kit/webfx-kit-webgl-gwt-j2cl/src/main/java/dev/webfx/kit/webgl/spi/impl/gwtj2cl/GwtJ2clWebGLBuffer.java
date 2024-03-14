package dev.webfx.kit.webgl.spi.impl.gwtj2cl;

import dev.webfx.kit.webgl.WebGLBuffer;

/**
 * @author Bruno Salmon
 */
public class GwtJ2clWebGLBuffer implements WebGLBuffer {

    final elemental2.webgl.WebGLBuffer jsWebGLBuffer;

    public GwtJ2clWebGLBuffer(elemental2.webgl.WebGLBuffer jsWebGLBuffer) {
        this.jsWebGLBuffer = jsWebGLBuffer;
    }
}
