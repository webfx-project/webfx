package dev.webfx.kit.webgl.spi.impl.gwtj2cl;

import dev.webfx.kit.webgl.WebGLFramebuffer;

/**
 * @author Bruno Salmon
 */
public class GwtJ2clWebGLFramebuffer implements WebGLFramebuffer {

    final elemental2.webgl.WebGLFramebuffer jsWebGLFramebuffer;

    public GwtJ2clWebGLFramebuffer(elemental2.webgl.WebGLFramebuffer jsWebGLFramebuffer) {
        this.jsWebGLFramebuffer = jsWebGLFramebuffer;
    }
}
