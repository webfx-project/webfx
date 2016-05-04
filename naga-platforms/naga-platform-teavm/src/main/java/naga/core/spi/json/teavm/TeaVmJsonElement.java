package naga.core.spi.json.teavm;

import naga.core.spi.json.JsonElement;
import naga.core.spi.json.JsonException;
import naga.core.valuesobject.RawType;
import naga.core.valuesobject.ValuesArray;
import naga.core.valuesobject.ValuesElement;
import naga.core.valuesobject.ValuesObject;
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
    public <SC extends ValuesElement> SC copy() {
        JSObject copy = JSUtil.copy(jsValue);
        return (SC) (isObject() ? TeaVmJsonObject.create(copy) : new TeaVmJsonArray(JSArray.create()));
    }

    @Override
    public final boolean isArray() {
        return getRawType(jsValue) == RawType.RAW_VALUES_ARRAY;
    }

    @Override
    public final boolean isObject() {
        return getRawType(jsValue) == RawType.RAW_VALUES_OBJECT;
    }

    @Override
    public RawType getRawType(Object rawValue) {
        JSObject jso = (JSObject) rawValue;
        switch(JSUtil.getJsType(jso)) {
            case "object": return JSUtil.isArray(jso) ? RawType.RAW_VALUES_ARRAY : RawType.RAW_VALUES_OBJECT;
            case "string":
            case "number":
            case "boolean": return RawType.RAW_SCALAR;
        }
        return RawType.OTHER;
    }

    @Override
    public <T> T wrapScalar(Object rawScalar) {
        return JSUtil.js2jScalar((JSObject) rawScalar);
    }

    @Override
    public TeaVmJsonArray wrapValuesArray(Object rawArray) {
        return TeaVmJsonArray.create((JSArray) rawArray);
    }

    @Override
    public TeaVmJsonObject wrapValuesObject(Object rawObject) {
        return TeaVmJsonObject.create((JSObject) rawObject);
    }

    @Override
    public JSObject unwrapScalar(Object scalar) {
        return JSUtil.j2jsScalar(scalar);
    }

    @Override
    public JSObject unwrapArray(ValuesArray array) {
        if (array == null)
            return null;
        return ((TeaVmJsonElement) array).getJsValue();
    }

    @Override
    public JSObject unwrapObject(ValuesObject object) {
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
