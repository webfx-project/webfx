package naga.core.spi.json.cn1;

import com.codename1.io.JSONParser;
import naga.core.composite.WritableCompositeArray;
import naga.core.composite.WritableCompositeObject;
import naga.core.composite.listmap.ListMapCompositeElement;

import java.io.IOException;
import java.io.StringReader;
import java.util.List;
import java.util.Map;

/**
 * @author Bruno Salmon
 */
public interface Cn1JsonElement extends ListMapCompositeElement {

    JSONParser cn1Parser = new JSONParser();

    @Override
    default Object parseNativeObject(String text) {
        try {
            return cn1Parser.parseJSON(new StringReader(text));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    default Object parseNativeArray(String text) {
        return null;
    }

    @Override
    default WritableCompositeObject nativeToCompositeObject(Object nativeObject) {
        if (nativeObject == null || nativeObject instanceof WritableCompositeObject)
            return (WritableCompositeObject) nativeObject;
        return new Cn1JsonObject((Map) nativeObject);
    }

    @Override
    default WritableCompositeArray nativeToCompositeArray(Object nativeArray) {
        if (nativeArray == null || nativeArray instanceof WritableCompositeArray)
            return (WritableCompositeArray) nativeArray;
        return new Cn1JsonArray((List) nativeArray);
    }

}