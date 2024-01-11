package dev.webfx.kit.webgl.spi.impl.gwt;

import dev.webfx.kit.webgl.WebGLShader;

/**
 * @author Bruno Salmon
 */
public class GwtWebGLShader implements WebGLShader {

    final elemental2.webgl.WebGLShader jsWebGLShader;

    public GwtWebGLShader(elemental2.webgl.WebGLShader jsWebGLShader) {
        this.jsWebGLShader = jsWebGLShader;
    }
}
