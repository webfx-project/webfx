package dev.webfx.kit.webgl.spi.impl.gwtj2cl;

import dev.webfx.kit.webgl.WebGLTexture;

/**
 * @author Bruno Salmon
 */
public class GwtJ2clWebGLTexture implements WebGLTexture {

    final elemental2.webgl.WebGLTexture jsWebGLTexture;

    public GwtJ2clWebGLTexture(elemental2.webgl.WebGLTexture jsWebGLTexture) {
        this.jsWebGLTexture = jsWebGLTexture;
    }
}
