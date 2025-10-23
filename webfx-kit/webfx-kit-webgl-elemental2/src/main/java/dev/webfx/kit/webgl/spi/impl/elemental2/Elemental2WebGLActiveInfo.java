package dev.webfx.kit.webgl.spi.impl.elemental2;

import dev.webfx.kit.webgl.WebGLActiveInfo;

/**
 * @author Bruno Salmon
 */
public class Elemental2WebGLActiveInfo implements WebGLActiveInfo {

    final elemental2.webgl.WebGLActiveInfo jsWebGLActiveInfo;

    public Elemental2WebGLActiveInfo(elemental2.webgl.WebGLActiveInfo jsWebGLActiveInfo) {
        this.jsWebGLActiveInfo = jsWebGLActiveInfo;
    }

    @Override
    public String getName() {
        return jsWebGLActiveInfo.name;
    }
}
