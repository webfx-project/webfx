package naga.core.spi.json.teavm;

import naga.core.spi.json.*;
import org.teavm.jso.JSObject;
import org.teavm.jso.core.JSArray;

/*
 * @author Bruno Salmon
 */
public final class TeaVmJsonFactory extends JsonFactory<JSArray, JSObject> {

    @Override
    public TeaVmJsonArray createArray() {
        return createNewArray(JSArray.create());
    }

    @Override
    protected TeaVmJsonArray createNewArray(JSArray teavmArray) {
        return new TeaVmJsonArray(teavmArray);
    }

    @Override
    public TeaVmJsonObject createObject() {
        return new TeaVmJsonObject();
    }

    @Override
    protected TeaVmJsonObject createNewObject(JSObject nativeObject) {
        return new TeaVmJsonObject(nativeObject);
    }

    @Override
    protected boolean isNativeValueSet(Object nativeValue) {
        return nativeValue == null || JSUtil.isUndefined((JSObject) nativeValue);
    }

    @Override
    protected Object parseNative(String jsonString) {
        return JSUtil.parse(jsonString);
    }

    @Override
    protected TeaVmJsonElement nativeToJsonElement(Object teavmElement) {
        if (teavmElement instanceof JSArray)
            return createNewArray((JSArray) teavmElement);
        return createNewObject((JSObject) teavmElement);
    }
}