package naga.core.spi.json.teavm;

import naga.core.spi.json.JsonElement;
import naga.core.spi.json.JsonException;
import naga.core.composite.*;
import naga.core.composite.CompositeElement;
import naga.core.composite.CompositeObject;
import org.teavm.jso.JSObject;
import org.teavm.jso.core.JSArray;

/*
 * @author Bruno Salmon
 */
abstract class TeaVmJsonElement extends TeaVmJsonValue implements JsonElement {

    public TeaVmJsonElement(JSObject jsValue) {
        super(jsValue);
    }

    @Override
    public final void clear() {
        if (isObject())
            clearObject();
        else
            clearArray();
    }

    private native void clearArray() /*-{
        this.length = 0;
    }-*/;

    private native void clearObject() /*-{
        for (var key in this) {
          if (Object.prototype.hasOwnProperty.call(this, key)) {
            delete this[key];
          }
        }
    }-*/;

    @Override
    public <SC extends CompositeElement> SC copy() {
        JSObject copy = JSUtil.copy(jsValue);
        return (SC) (isObject() ? TeaVmJsonObject.create(copy) : new TeaVmJsonArray(JSArray.create()));
    }

    @Override
    public JSObject createNativeObject() {
        return JSUtil.newJSObject();
    }

    @Override
    public JSObject createNativeArray() {
        return JSUtil.newJSArray();
    }

    @Override
    public JSObject parseNativeObject(String text) {
        return JSUtil.parse(text);
    }

    @Override
    public JSObject parseNativeArray(String text) {
        return JSUtil.parse(text);
    }

    @Override
    public JSObject getNativeElement() {
        return jsValue;
    }

    @Override
    public ElementType getNativeElementType(Object nativeElement) {
        JSObject jso = (JSObject) nativeElement;
        switch(JSUtil.getJsType(jso)) {
            case "object": return JSUtil.isArray(jso) ? ElementType.ARRAY : ElementType.OBJECT;
            case "string":
            case "number":
            case "boolean": return ElementType.SCALAR;
        }
        return ElementType.UNKNOWN;
    }

    @Override
    public <T> T nativeToCompositeScalar(Object nativeScalar) {
        return JSUtil.js2jScalar((JSObject) nativeScalar);
    }

    @Override
    public TeaVmJsonArray nativeToCompositeArray(Object nativeArray) {
        return TeaVmJsonArray.create((JSArray) nativeArray);
    }

    @Override
    public TeaVmJsonObject nativeToCompositeObject(Object nativeObject) {
        return TeaVmJsonObject.create((JSObject) nativeObject);
    }

    @Override
    public JSObject compositeToNativeScalar(Object scalar) {
        return JSUtil.j2jsScalar(scalar);
    }

    @Override
    public JSObject compositeToNativeObject(CompositeObject object) {
        if (object == null)
            return null;
        return ((TeaVmJsonElement) object).getJsValue();
    }

    @Override
    public final String toJsonString() {
        try {
            return super.toJson();
        } catch (Exception e) {
            throw new JsonException("Failed to encode as JSON: " + e.getMessage());
        }
    }

    @Override
    public String toString() {
        return toJsonString();
    }
}
