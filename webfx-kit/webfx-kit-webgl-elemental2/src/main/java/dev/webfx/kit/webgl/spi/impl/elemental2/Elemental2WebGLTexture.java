package dev.webfx.kit.webgl.spi.impl.elemental2;

import dev.webfx.kit.webgl.WebGLTexture;

/**
 * @author Bruno Salmon
 */
public class Elemental2WebGLTexture implements WebGLTexture {

    final elemental2.webgl.WebGLTexture jsWebGLTexture;

    public Elemental2WebGLTexture(elemental2.webgl.WebGLTexture jsWebGLTexture) {
        this.jsWebGLTexture = jsWebGLTexture;
    }
}
