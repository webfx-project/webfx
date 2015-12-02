package naga.core.spi.json.teavm;

import naga.core.spi.json.JsonException;
import naga.core.spi.json.JsonFactory;
import org.teavm.jso.JSObject;
import org.teavm.jso.core.JSArray;

/*
 * @author Bruno Salmon
 */
public final class TeaVmJsonFactory implements JsonFactory<JSArray, JSObject> {

    @Override
    public TeaVmJsonArray createArray() {
        return TeaVmJsonArray.create();
    }

    @Override
    public TeaVmJsonArray createArray(JSArray nativeArray) {
        return TeaVmJsonArray.create(nativeArray);
    }

    @Override
    public TeaVmJsonObject createObject() {
        return TeaVmJsonObject.create();
    }

    @Override
    public TeaVmJsonObject createObject(JSObject nativeObject) {
        return TeaVmJsonObject.create(nativeObject);
    }

    @Override
    public TeaVmJsonObject parse(String jsonString) throws JsonException {
        return TeaVmJsonObject.create(JSUtil.parse(jsonString));
    }

}