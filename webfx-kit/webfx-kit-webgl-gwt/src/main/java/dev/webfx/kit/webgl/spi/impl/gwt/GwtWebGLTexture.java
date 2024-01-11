package dev.webfx.kit.webgl.spi.impl.gwt;

import dev.webfx.kit.webgl.WebGLTexture;

/**
 * @author Bruno Salmon
 */
public class GwtWebGLTexture implements WebGLTexture {

    final elemental2.webgl.WebGLTexture jsWebGLTexture;

    public GwtWebGLTexture(elemental2.webgl.WebGLTexture jsWebGLTexture) {
        this.jsWebGLTexture = jsWebGLTexture;
    }
}
