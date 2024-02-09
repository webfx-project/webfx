package dev.webfx.kit.launcher.spi.impl.gwt;

import elemental2.promise.Promise;
import jsinterop.annotations.JsMethod;
import jsinterop.annotations.JsPackage;
import jsinterop.annotations.JsType;

/**
 * @author Bruno Salmon
 */
@JsType(namespace = JsPackage.GLOBAL)
public final class JsClipboard {

    @JsMethod(namespace = "navigator.clipboard")
    public static native void writeText(String text);

    @JsMethod(namespace = "navigator.clipboard")
    public static native Promise<String> readText();

}
