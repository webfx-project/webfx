package dev.webfx.kit.registry.javafxweb;

import elemental2.core.Function;
import elemental2.dom.DomGlobal;
import jsinterop.base.Js;
import jsinterop.base.JsPropertyMap;
import netscape.javascript.JSException;
import netscape.javascript.JSObject;

/**
 * @author Bruno Salmon
 */
final class Elemental2JSObject extends JSObject {

    private final JsPropertyMap<Object> jsMap;

    public Elemental2JSObject(Object javaScriptObject) {
        jsMap = Js.asPropertyMap(javaScriptObject);
    }

    @Override
    public Object call(String methodName, Object... args) throws JSException {
        return call(jsMap, methodName, args);
    }

    @Override
    public Object eval(String s) throws JSException {
        return eval(jsMap, s);
    }

    @Override
    public Object getMember(String name) throws JSException {
        Object result = jsMap.get(name);
        return wrapJSObject(result);
    }

    @Override
    public void setMember(String name, Object value) throws JSException {
        bindCallbackMethods(value);
        jsMap.set(name, value);
    }

    @Override
    public void removeMember(String name) throws JSException {
        jsMap.delete(name);
    }

    @Override
    public Object getSlot(int index) throws JSException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setSlot(int index, Object value) throws JSException {
        throw new UnsupportedOperationException();
    }

    private static void bindCallbackMethods(Object javaInstance) {
        WebToJavaCallbacks.bindCallbackMethods(javaInstance);
    }

    public static Object wrapJSObject(Object o) {
        if ("object".equals(Js.typeof(o)))
            o = new Elemental2JSObject(o);
        return o;
    }

    public static Object call(Object javaScriptObject, String methodName, Object... args) throws JSException {
        return call(Js.asPropertyMap(javaScriptObject), methodName, args);
    }

    public static Object call(JsPropertyMap<Object> jsMap, String methodName, Object... args) throws JSException {
        Function f = (Function) jsMap.get(methodName);
        if (f == null) {
            DomGlobal.console.log("Function '" + methodName + "' not found on following object:");
            DomGlobal.console.log(jsMap);
            throw new IllegalArgumentException("Function '" + methodName + "' not found on object '" + jsMap + "'");
        }
        return callFunctionAndWrapResult(f, jsMap, args);
    }

    public static Object eval(Object javaScriptObject, String script) throws JSException {
        return call(Js.asPropertyMap(javaScriptObject), "eval", script);
    }

    private static Object callFunctionAndWrapResult(Function f, Object o, Object... args) {
        Object result = f.apply(o, args);
        return wrapJSObject(result);
    }

}
