package dev.webfx.kit.launcher.spi.impl.gwtj2cl;

import elemental2.promise.Promise;
import jsinterop.annotations.JsPackage;
import jsinterop.annotations.JsType;

/**
 * @author Bruno Salmon
 */
@JsType(isNative = true, namespace = JsPackage.GLOBAL, name = "navigator.clipboard")
public final class Clipboard {

    public static native void writeText(String text);

    public static native Promise<String> readText();

}
