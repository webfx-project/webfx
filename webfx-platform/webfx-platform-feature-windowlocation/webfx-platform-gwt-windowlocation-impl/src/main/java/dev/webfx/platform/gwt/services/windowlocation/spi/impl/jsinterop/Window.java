package dev.webfx.platform.gwt.services.windowlocation.spi.impl.jsinterop;

import com.google.gwt.core.client.JavaScriptObject;
import jsinterop.annotations.JsFunction;
import jsinterop.annotations.JsType;

import static jsinterop.annotations.JsPackage.GLOBAL;


/**
 * @author Bruno Salmon
 */

@JsType(namespace = GLOBAL, name = "window", isNative = true)
public final class Window {

    //public static History history;

    public static EventHandler onafterprint;
    public static EventHandler onbeforeprint;
    public static OnBeforeUnloadEventHandler onbeforeunload;
    public static EventHandler onhashchange;
    public static EventHandler onlanguagechange;
    public static EventHandler onmessage;
    public static EventHandler onoffline;
    public static EventHandler ononline;
    public static EventHandler onpagehide;
    public static EventHandler onpageshow;
    public static OnPopStateListener onpopstate;
    public static EventHandler onrejectionhandled;
    public static EventHandler onstorage;
    public static EventHandler onunhandledrejection;
    public static EventHandler onunload;

    @JsFunction
    public interface EventHandler {
        void onEvent(JavaScriptObject event);
    }

    @JsFunction
    public interface OnBeforeUnloadEventHandler {
        String onBeforeUnload(JavaScriptObject event);
    }

    @JsFunction
    public interface OnPopStateListener {
        void onPopState(PopStateEvent event);
    }

    @JsType(isNative = true)
    public static final class PopStateEvent {
        public JavaScriptObject state;
    }
}
