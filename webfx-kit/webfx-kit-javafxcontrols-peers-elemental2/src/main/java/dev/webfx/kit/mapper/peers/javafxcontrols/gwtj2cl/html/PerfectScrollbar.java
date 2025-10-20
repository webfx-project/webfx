package dev.webfx.kit.mapper.peers.javafxcontrols.gwtj2cl.html;

import elemental2.dom.Element;
import jsinterop.annotations.JsConstructor;
import jsinterop.annotations.JsMethod;
import jsinterop.annotations.JsPackage;
import jsinterop.annotations.JsType;
import jsinterop.base.JsPropertyMap;

/**
 * @author Bruno Salmon
 */
@JsType(isNative = true, namespace = JsPackage.GLOBAL, name = "PerfectScrollbar")
public class PerfectScrollbar {

    @JsConstructor
    public PerfectScrollbar(Element psContainer, JsPropertyMap<?> options) {}

    @JsMethod
    public native void update();

}
