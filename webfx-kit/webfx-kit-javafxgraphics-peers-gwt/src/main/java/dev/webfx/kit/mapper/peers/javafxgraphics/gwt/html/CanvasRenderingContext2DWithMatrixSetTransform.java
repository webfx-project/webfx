package dev.webfx.kit.mapper.peers.javafxgraphics.gwt.html;

import elemental2.dom.CanvasRenderingContext2D;
import elemental2.dom.DOMMatrixReadOnly;
import jsinterop.annotations.JsMethod;
import jsinterop.annotations.JsPackage;
import jsinterop.annotations.JsType;

/**
 * @author Bruno Salmon
 */
@JsType(isNative = true, namespace = JsPackage.GLOBAL, name = "CanvasRenderingContext2D")
final class CanvasRenderingContext2DWithMatrixSetTransform extends CanvasRenderingContext2D {

    @JsMethod
    public native void setTransform(DOMMatrixReadOnly transform);

}
