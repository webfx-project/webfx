package naga.providers.platform.client.teavm.services.json;

import naga.platform.services.json.WritableJsonArray;
import org.teavm.jso.JSObject;
import org.teavm.jso.core.JSArray;

/**
 * TeaVM implementation of JsonArray.
 *
 * @author Bruno Salmon
 */
public final class TeaVmJsonArray extends TeaVmJsonElement implements WritableJsonArray {

    public static TeaVmJsonArray create(JSArray jsArray) {
        if (jsArray == null || TeaVmJsonElement.isUndefined(jsArray))
            return null;
        return new TeaVmJsonArray(jsArray);
    }

    TeaVmJsonArray(JSArray jsArray) {
        super(jsArray);
    }

    <T extends JSObject> JSArray<T> asArray() { return nativeElement.cast(); }

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

    public <V> V getElement(int index) {
        return anyNativeToJava(getNativeElement(index));
    }

    @Override
    public native <T> T remove(int index) /*-{
        return this.splice(index, 1)[0];
    }-*/;

}