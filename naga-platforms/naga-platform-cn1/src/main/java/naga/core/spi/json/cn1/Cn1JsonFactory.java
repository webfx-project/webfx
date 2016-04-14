package naga.core.spi.json.cn1;

import com.codename1.io.JSONParser;
import naga.core.spi.json.JsonArray;
import naga.core.spi.json.JsonObject;
import naga.core.spi.json.listmap.ListMapJsonFactory;

import java.io.IOException;
import java.io.StringReader;
import java.util.List;
import java.util.Map;

/**
 * @author Bruno Salmon
 */
public final class Cn1JsonFactory extends ListMapJsonFactory {

    public static Cn1JsonFactory SINGLETON = new Cn1JsonFactory();

    private final JSONParser cn1Parser = new JSONParser();
    private Cn1JsonFactory() {
        cn1Parser.setUseLongs(true);
    }

    @Override
    public Cn1JsonArray createArray() {
        return new Cn1JsonArray();
    }

    @Override
    protected JsonArray createNewArray(List nativeArray) {
        return new Cn1JsonArray(nativeArray);
    }

    @Override
    public Cn1JsonObject createObject() {
        return new Cn1JsonObject();
    }

    @Override
    protected JsonObject createNewObject(Map<String, Object> nativeObject) {
        return new Cn1JsonObject(nativeObject);
    }

    @Override
    public Object parseNative(String jsonString) {
        try {
            return cn1Parser.parseJSON(new StringReader(jsonString));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
