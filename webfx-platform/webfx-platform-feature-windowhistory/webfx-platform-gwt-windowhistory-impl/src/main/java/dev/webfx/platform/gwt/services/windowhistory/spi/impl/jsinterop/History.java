package dev.webfx.platform.gwt.services.windowhistory.spi.impl.jsinterop;

import com.google.gwt.core.client.JavaScriptObject;
import jsinterop.annotations.JsType;

import static jsinterop.annotations.JsPackage.GLOBAL;

/**
 * @author Bruno Salmon
 */
@JsType(namespace = GLOBAL, isNative = true)
public final class History {

    public int length;

    public native void go(int offset);

    public JavaScriptObject state;

    public native void pushState(JavaScriptObject stateObj, String title, String url);

    public native void replaceState(JavaScriptObject stateObj, String title, String url);

}
