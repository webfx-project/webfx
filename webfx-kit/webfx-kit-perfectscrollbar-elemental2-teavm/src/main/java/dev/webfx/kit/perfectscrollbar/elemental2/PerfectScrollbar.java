package dev.webfx.kit.perfectscrollbar.elemental2;

import elemental2.dom.Element;
import jsinterop.base.JsPropertyMap;
import org.teavm.jso.JSClass;
import org.teavm.jso.JSObject;

/**
 * @author Bruno Salmon
 */
@JSClass
public class PerfectScrollbar implements JSObject {

    public PerfectScrollbar(Element psContainer, JsPropertyMap<?> options) {}

    public native void update();

}
