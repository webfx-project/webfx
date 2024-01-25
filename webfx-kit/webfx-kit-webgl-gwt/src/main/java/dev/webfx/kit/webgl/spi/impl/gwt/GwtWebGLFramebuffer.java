package dev.webfx.kit.webgl.spi.impl.gwt;

import dev.webfx.kit.webgl.WebGLFramebuffer;

/**
 * @author Bruno Salmon
 */
public class GwtWebGLFramebuffer implements WebGLFramebuffer {

    final elemental2.webgl.WebGLFramebuffer jsWebGLFramebuffer;

    public GwtWebGLFramebuffer(elemental2.webgl.WebGLFramebuffer jsWebGLFramebuffer) {
        this.jsWebGLFramebuffer = jsWebGLFramebuffer;
    }
}
