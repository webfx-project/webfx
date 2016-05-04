package naga.core.spi.json.teavm;

import naga.core.spi.json.JsonArray;
import org.teavm.jso.JSObject;
import org.teavm.jso.core.JSArray;

import java.util.Collection;

/**
 * Client-side implementation of JsonArray.
 *
 * @author Bruno Salmon
 */
final class TeaVmJsonArray extends TeaVmJsonElement implements JsonArray {

    public static TeaVmJsonArray create(JSArray jsArray) {
        if (jsArray == null || JSUtil.isUndefined(jsArray))
            return null;
        return new TeaVmJsonArray(jsArray);
    }

    TeaVmJsonArray(JSArray jsArray) {
        super(jsArray);
    }

    <T extends JSObject> JSArray<T> asArray() { return jsValue.cast(); }

    @Override
    public JSObject getRaw(int index) {
        return asArray().get(index);
    }

    @Override
    public native void setRaw(int index, Object value);

    @Override
    public native Collection values();

    public native int indexOfRaw(Object value);

    @Override
    public int size() {
        return asArray().getLength();
    }

    @Override
    public void pushRaw(Object value) {
        asArray().push((JSObject) value);
    }

    @Override
    public native <T> T remove(int index) /*-{
        return this.splice(index, 1)[0];
    }-*/;

}