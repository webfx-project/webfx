package dev.webfx.kit.registry.javafxweb;

import com.google.gwt.core.client.JavaScriptObject;
import jsinterop.base.Js;
import netscape.javascript.JSException;
import netscape.javascript.JSObject;

/**
 * @author Bruno Salmon
 */
final class GwtJSObject extends JSObject {

    private final JavaScriptObject javaScriptObject;

    public GwtJSObject(JavaScriptObject javaScriptObject) {
        this.javaScriptObject = javaScriptObject;
    }

    @Override
    public Object call(String methodName, Object... args) throws JSException {
        Object result = call(javaScriptObject, methodName, args);
        return GwtJSObject.wrapJSObject(result);
    }

    @Override
    public Object eval(String s) throws JSException {
        Object result = eval(javaScriptObject, s);
        return wrapJSObject(result);
    }

    @Override
    public Object getMember(String name) throws JSException {
        Object result = getJavaScriptObjectAttribute(javaScriptObject, name);
        return wrapJSObject(result);
    }

    @Override
    public void setMember(String name, Object value) throws JSException {
        bindCallbackMethods(value);
        setJavaScriptObjectAttribute(javaScriptObject, name, value);
    }

    @Override
    public void removeMember(String name) throws JSException {
        removeJavaScriptObjectAttribute(javaScriptObject, name);
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
        WebToJavaCallbacks.bindCallbackMethods(javaInstance, javaInstance.getClass().getName());
    }

    static Object wrapJSObject(Object o) {
        if ("object".equals(Js.typeof(o)))
            o = new GwtJSObject((JavaScriptObject) o);
        return o;
    }

    private static native void setJavaScriptObjectAttribute(JavaScriptObject o, String name, Object value) /*-{
        o[name] = value;
    }-*/;

    private static native Object getJavaScriptObjectAttribute(JavaScriptObject o, String name) /*-{
        return o[name];
    }-*/;

    private static native Object removeJavaScriptObjectAttribute(JavaScriptObject o, String name) /*-{
        delete o[name];
    }-*/;

    native static Object eval(Object o, String script)/*-{
        return o.eval(script);
    }-*/;

    native static Object call(JavaScriptObject o, String methodName, Object... args)/*-{
        var f = o[methodName];
        return f.apply(this, args);
    }-*/;

}
