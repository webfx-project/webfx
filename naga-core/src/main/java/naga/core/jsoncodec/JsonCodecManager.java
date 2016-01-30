package naga.core.jsoncodec;

import naga.core.buscall.BusCallService;
import naga.core.spi.json.Json;
import naga.core.spi.json.JsonArray;
import naga.core.spi.json.JsonObject;
import naga.core.spi.sql.SqlArgument;
import naga.core.spi.sql.SqlReadResult;

import java.util.HashMap;
import java.util.Map;

/*
 * @author Bruno Salmon
 */

public class JsonCodecManager {

    private static final String CODEC_ID_KEY = "$codec";

    private static final Map<Class, JsonCodec> encoders = new HashMap<>();
    private static final Map<String, JsonCodec> decoders = new HashMap<>();

    static {
        // Registering all required json codecs (especially for network calls)
        BusCallService.registerJsonCodecs();
        SqlArgument.registerJsonCodec();
        SqlReadResult.registerJsonCodec();
    }

    public static  <J> void registerJsonCodec(Class<? extends J> javaClass, JsonCodec<J> jsonCodec) {
        encoders.put(javaClass, jsonCodec);
        decoders.put(jsonCodec.getCodecID(), jsonCodec);
    }

    public static void useSameJsonCodecAs(Class javaClass1, Class javaClass2) {
        registerJsonCodec(javaClass1, getJsonEncoder(javaClass2));
    }

    public static <J> JsonCodec<J> getJsonEncoder(Class<J> javaClass) {
        for (Class c = javaClass; c != null; c = c.getSuperclass()) {
            JsonCodec jsonCodec = encoders.get(c);
            if (jsonCodec != null)
                return jsonCodec;
        }
        return null;
    }

    public static <J> JsonCodec<J> getJsonDecoder(String jsonUID) {
        return decoders.get(jsonUID);
    }

    public static Object encodeToJson(Object object) {
        if (object == null || object instanceof String || object instanceof Number)
            return object;
        return encodeToJsonObject(object);
    }

    public static JsonObject encodeToJsonObject(Object object) {
        if (object == null)
            return null;
        if (object instanceof JsonObject)
            return (JsonObject) object;
        JsonObject json = Json.createObject();
        encodeJavaObjectToJsonObject(object, json);
        return json;
    }

    public static void encodeJavaObjectToJsonObject(Object javaObject, JsonObject json) {
        JsonCodec encoder = getJsonEncoder(javaObject.getClass());
        if (encoder == null)
            throw new IllegalArgumentException("No JsonCodec for type: " + javaObject.getClass());
        json.set(CODEC_ID_KEY, encoder.getCodecID());
        encoder.encodeToJson(javaObject, json);
    }

    public static <J> J decodeFromJson(Object object) {
        if (object instanceof JsonObject)
            return decodeFromJsonObject((JsonObject) object);
        return (J) object;
    }

    public static <J> J decodeFromJsonObject(JsonObject json) {
        if (json == null)
            return null;
        String codecID = json.getString(CODEC_ID_KEY);
        JsonCodec<J> decoder = getJsonDecoder(codecID);
        if (decoder == null)
            throw new IllegalArgumentException("No JsonCodec for id: '" + codecID + "'");
        return decoder.decodeFromJson(json);
    }


    public static JsonArray encodePrimitiveArrayToJsonArray(Object[] primArray) {
        if (primArray == null)
            return null;
        JsonArray jsonArray = Json.createArray();
        for (Object value : primArray)
            jsonArray.push(value);
        return jsonArray;
    }

    public static Object[] decodePrimitiveArrayFromJsonArray(JsonArray jsonArray) {
        if (jsonArray == null)
            return null;
        int n = jsonArray.length();
        Object[] array = new Object[n];
        for (int i = 0; i < n; i++)
            array[i] = jsonArray.get(i);
        return array;
    }

    public static JsonArray encodeToJsonArray(Object[] array) {
        if (array == null)
            return null;
        JsonArray jsonArray = Json.createArray();
        for (Object object : array)
            jsonArray.push(encodeToJsonObject(object));
        return jsonArray;
    }

    /* Doesn't compile with GWT
    public static <A> A[] deserializeObjectsArray(JsonArray jsonArray, Class<A> expectedClass) {
        if (jsonArray == null)
            return null;
        int n = jsonArray.length();
        A[] array = (A[]) Array.newInstance(expectedClass, n);
        for (int i = 0; i < n; i++)
            array[i] = deserialize(jsonArray.getObject(i));
        return array;
    }
    */
}
