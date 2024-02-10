package dev.webfx.kit.mapper.peers.javafxcontrols.gwt.html;

import elemental2.dom.Element;
import jsinterop.annotations.JsConstructor;
import jsinterop.annotations.JsMethod;
import jsinterop.annotations.JsType;
import jsinterop.base.JsPropertyMap;

/**
 * @author Bruno Salmon
 */
@JsType(isNative = true, namespace = "window")
public class PerfectScrollbar {

    @JsConstructor
    public PerfectScrollbar(Element psContainer, JsPropertyMap<?> options) {}

    @JsMethod
    public native void update();

}
