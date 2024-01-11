package dev.webfx.kit.webgl.spi.impl.gwt;

import dev.webfx.kit.webgl.ArrayBuffer;
import elemental2.core.ArrayBufferView;

/**
 * @author Bruno Salmon
 */
public class GwtArrayBuffer implements ArrayBuffer {

    final elemental2.core.ArrayBuffer jsArrayBuffer;
    final ArrayBufferView jsArrayBufferView;

    public GwtArrayBuffer(elemental2.core.ArrayBuffer jsArrayBuffer) {
        this.jsArrayBuffer = jsArrayBuffer;
        this.jsArrayBufferView = null;
    }

    public GwtArrayBuffer(ArrayBufferView jsArrayBufferView) {
        this.jsArrayBuffer = null;
        this.jsArrayBufferView = jsArrayBufferView;
    }
}
