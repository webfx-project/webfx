package dev.webfx.kit.webgl.spi.impl.gwt;

import dev.webfx.kit.webgl.WebGLActiveInfo;

/**
 * @author Bruno Salmon
 */
public class GwtWebGLActiveInfo implements WebGLActiveInfo {

    final elemental2.webgl.WebGLActiveInfo jsWebGLActiveInfo;

    public GwtWebGLActiveInfo(elemental2.webgl.WebGLActiveInfo jsWebGLActiveInfo) {
        this.jsWebGLActiveInfo = jsWebGLActiveInfo;
    }

    @Override
    public String getName() {
        return jsWebGLActiveInfo.name;
    }
}
