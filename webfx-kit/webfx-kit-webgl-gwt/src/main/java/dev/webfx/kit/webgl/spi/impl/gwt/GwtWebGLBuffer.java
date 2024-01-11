package dev.webfx.kit.webgl.spi.impl.gwt;

import dev.webfx.kit.webgl.WebGLBuffer;

/**
 * @author Bruno Salmon
 */
public class GwtWebGLBuffer implements WebGLBuffer {

    final elemental2.webgl.WebGLBuffer jsWebGLBuffer;

    public GwtWebGLBuffer(elemental2.webgl.WebGLBuffer jsWebGLBuffer) {
        this.jsWebGLBuffer = jsWebGLBuffer;
    }
}
