package naga.core.spi.json.teavm;

import naga.core.spi.json.JsonArray;
import naga.core.spi.json.JsonException;
import naga.core.spi.json.JsonFactory;
import naga.core.spi.json.JsonObject;

/*
 * @author Bruno Salmon
 */
public class TeaVmJsonFactory implements JsonFactory {

    @Override
    public JsonArray createArray() {
        return TeaVmJsonArray.create();
    }

    @Override
    public JsonObject createObject() {
        return TeaVmJsonObject.create();
    }

    @Override
    public TeaVmJsonObject parse(String jsonString) throws JsonException {
        return TeaVmJsonObject.create(JSUtil.parse(jsonString));
    }
}