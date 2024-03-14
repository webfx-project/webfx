package dev.webfx.kit.webgl.spi.impl.gwtj2cl;

import dev.webfx.kit.webgl.WebGLActiveInfo;

/**
 * @author Bruno Salmon
 */
public class GwtJ2clWebGLActiveInfo implements WebGLActiveInfo {

    final elemental2.webgl.WebGLActiveInfo jsWebGLActiveInfo;

    public GwtJ2clWebGLActiveInfo(elemental2.webgl.WebGLActiveInfo jsWebGLActiveInfo) {
        this.jsWebGLActiveInfo = jsWebGLActiveInfo;
    }

    @Override
    public String getName() {
        return jsWebGLActiveInfo.name;
    }
}
