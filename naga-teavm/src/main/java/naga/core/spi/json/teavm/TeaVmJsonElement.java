package naga.core.spi.json.teavm;

import naga.core.spi.json.JsonElement;
import naga.core.spi.json.JsonException;
import naga.core.spi.json.JsonType;
import org.teavm.jso.JSObject;

/*
 * @author Bruno Salmon
 */
abstract class TeaVmJsonElement extends TeaVmJsonValue implements JsonElement {

    public TeaVmJsonElement(JSObject jsValue) {
        super(jsValue);
    }

    @Override
    @SuppressWarnings("unchecked")
    public final <T extends JsonElement> T clear() {
        return (T) (isObject() ? clearObject() : clearArray());
    }

    @Override
    public final <T extends JsonElement> T copy() {
        JSObject copy = JSUtil.copy(jsValue);
        return (T) (isObject() ? TeaVmJsonObject.create(copy) : TeaVmJsonArray.create());
    }

    @Override
    public final boolean isArray() {
        return getType() == JsonType.ARRAY;
    }

    @Override
    public final boolean isObject() {
        return getType() == JsonType.OBJECT;
    }

    @Override
    public final String toJsonString() {
        try {
            return super.toJson();
        } catch (Exception e) {
            throw new JsonException("Failed to encode as JSON: " + e.getMessage());
        }
    }

    private TeaVmJsonElement clearArray() {return this; } /*-{
    this.length = 0;
    return this;
  }-*/;

    private TeaVmJsonElement clearObject() {return this; } /*-{
    for (var key in this) {
      if (Object.prototype.hasOwnProperty.call(this, key)) {
        delete this[key];
      }
    }
    return this;
  }-*/;
}
