package naga.core.spi.platform.client.teavm;

import naga.core.json.WritableJsonArray;
import org.teavm.jso.JSObject;
import org.teavm.jso.core.JSArray;

/**
 * TeaVM implementation of JsonArray.
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
    public TeaVmJsonArray setNativeElement(int index, Object value) {
        asArray().set(index, (JSObject) value);
        return this;
    }

    public native int indexOfNativeElement(Object element);

    @Override
    public int size() {
        return asArray().getLength();
    }

    @Override
    public TeaVmJsonArray pushNativeElement(Object element) {
        asArray().push((JSObject) element);
        return this;
    }

    @Override
    public native <T> T remove(int index) /*-{
        return this.splice(index, 1)[0];
    }-*/;

}