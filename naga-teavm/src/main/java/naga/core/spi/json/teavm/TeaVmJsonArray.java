package naga.core.spi.json.teavm;

import naga.core.spi.json.JsonArray;
import naga.core.spi.json.JsonType;
import org.teavm.jso.JSObject;
import org.teavm.jso.core.JSArray;

/**
 * Client-side implementation of JsonArray.
 *
 * @author Bruno Salmon
 */
final class TeaVmJsonArray extends TeaVmJsonElement implements JsonArray {

    TeaVmJsonArray(JSArray jsArray) {
        super(jsArray);
    }

    <T extends JSObject> JSArray<T> asArray() { return jsValue.cast(); }

    @Override
    public <T> void forEach(ListIterator<T> handler) {
        JSArray<JSObject> array = asArray();
        int length = array.getLength();
        System.out.println("forEach length = " + length);
        for (int i = 0; i < length; i++)
            handler.call(i, array.get(i).cast());
    } /*-{
    if (Array.prototype.forEach) {
      Array.prototype.forEach.call(this, function(item, index, array) {
        handler.
        @com.goodow.realtime.json.JsonArray.ListIterator::call(ILjava/lang/Object;)
        (index, item);
      });
    } else {
      var len = this.length;  // must be fixed during loop...
      for (var i = 0; i < len; i++) {
        handler.
        @com.goodow.realtime.json.JsonArray.ListIterator::call(ILjava/lang/Object;)
        (i, this[i]);
      }
    }
  }-*/;

    @SuppressWarnings("unchecked")
    @Override
    public TeaVmJsonElement get(int index) {
        return get0(index).cast();
    }

    @Override
    public TeaVmJsonArray getArray(int index) {
        return (TeaVmJsonArray) get(index);
    }

    @Override
    public native boolean getBoolean(int index);

    @Override
    public native double getNumber(int index);

    @Override
    public TeaVmJsonObject getObject(int index) {
        return (TeaVmJsonObject) get(index);
    }

    @Override
    public native String getString(int index);

    @Override
    public JsonType getType(int index) {
        return JSUtil.getType(get0(index));
    }

    public native int indexOf(Object value);

    @Override
    public native JsonArray insert(int index, Object element) /*-{
    this.splice(index, 0, element);
    return this;
  }-*/;

    @Override
    public int length() {
        return asArray().getLength();
    }

    @Override
    public native JsonArray push(boolean bool_) /*-{
    this[this.length] = bool_;
    return this;
  }-*/;

    @Override
    public native JsonArray push(double number) /*-{
    this[this.length] = number;
    return this;
  }-*/;

    @Override
    public JsonArray push(Object element) {
        asArray().push(JSUtil.j2js(element));
        return this;
    } /*-{
    this[this.length] = element;
    return this;
  }-*/;

    @Override
    public native <T> T remove(int index) /*-{
    return this.splice(index, 1)[0];
  }-*/;

    @Override
    public boolean removeValue(Object value) {
        int idx = indexOf(value);
        if (idx == -1) {
            return false;
        }
        remove(idx);
        return true;
    }

    private JSObject get0(int index) {
        return asArray().get(index);
    };
}