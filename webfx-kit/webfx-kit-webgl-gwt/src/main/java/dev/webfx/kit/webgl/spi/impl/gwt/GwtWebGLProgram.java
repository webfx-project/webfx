package dev.webfx.kit.webgl.spi.impl.gwt;

import dev.webfx.kit.webgl.WebGLProgram;

/**
 * @author Bruno Salmon
 */
public class GwtWebGLProgram implements WebGLProgram {

    final elemental2.webgl.WebGLProgram jsWebGLProgram;

    public GwtWebGLProgram(elemental2.webgl.WebGLProgram jsWebGLProgram) {
        this.jsWebGLProgram = jsWebGLProgram;
    }
}
