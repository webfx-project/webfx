package dev.webfx.kit.webgl.spi.impl.gwtj2cl;

import dev.webfx.kit.webgl.WebGLFramebuffer;

/**
 * @author Bruno Salmon
 */
public class Elemental2WebGLFramebuffer implements WebGLFramebuffer {

    final elemental2.webgl.WebGLFramebuffer jsWebGLFramebuffer;

    public Elemental2WebGLFramebuffer(elemental2.webgl.WebGLFramebuffer jsWebGLFramebuffer) {
        this.jsWebGLFramebuffer = jsWebGLFramebuffer;
    }
}
