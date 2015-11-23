package naga.core.spi.json.teavm;

import naga.core.spi.json.JsonType;
import org.teavm.jso.JSObject;

/**
 * JSO backed implementation of JsonValue.
 *
 * @author Bruno Salmon
 */
public class TeaVmJsonValue<T extends JSObject> {

    protected final T jsValue; // As opposed to GWT, TeaVM needs the value to be embed as a member field

    protected TeaVmJsonValue(T jsValue) {
        this.jsValue = jsValue;
    }

    public final JsonType getType() {
        return JSUtil.getType(jsValue);
    }

    public final String toJson() {
        return JSUtil.stringify(jsValue);
    }

    public T getJsValue() {
        return jsValue;
    }
}