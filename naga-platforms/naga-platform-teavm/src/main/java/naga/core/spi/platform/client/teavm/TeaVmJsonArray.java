package naga.core.spi.platform.client.teavm;

import naga.core.json.WritableJsonArray;
import org.teavm.jso.JSObject;
import org.teavm.jso.core.JSArray;

/**
 * Client-side implementation of JsonArray.
 *
 * @author Bruno Salmon
 */
final class TeaVmJsonArray extends TeaVmJsonElement implements WritableJsonArray {

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
    public JSObject getNativeElement(int index) {
        return asArray().get(index);
    }

    @Override
    public native void setNativeElement(int index, Object value);


    public native int indexOfNativeElement(Object element);

    @Override
    public int size() {
        return asArray().getLength();
    }

    @Override
    public void pushNativeElement(Object element) {
        asArray().push((JSObject) element);
    }

    @Override
    public native <T> T remove(int index) /*-{
        return this.splice(index, 1)[0];
    }-*/;

}