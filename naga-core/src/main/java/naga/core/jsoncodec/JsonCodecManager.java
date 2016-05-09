package naga.core.jsoncodec;

import naga.core.buscall.BusCallService;
import naga.core.composite.CompositeArray;
import naga.core.composite.CompositeObject;
import naga.core.composite.WritableCompositeArray;
import naga.core.composite.WritableCompositeObject;
import naga.core.spi.json.Json;
import naga.core.spi.sql.SqlArgument;
import naga.core.spi.sql.SqlReadResult;
import naga.core.util.Numbers;
import naga.core.util.async.Batch;

import java.lang.reflect.Array;
import java.util.HashMap;
import java.util.Map;

/*
 * @author Bruno Salmon
 */

public class JsonCodecManager {

    private static final String CODEC_ID_KEY = "$codec";

    private static final Map<Class, JsonCodec> encoders = new HashMap<>();
    private static final Map<String, JsonCodec> decoders = new HashMap<>();

    public static  <J> void registerJsonCodec(Class<? extends J> javaClass, JsonCodec<J> jsonCodec) {
        encoders.put(javaClass, jsonCodec);
        decoders.put(jsonCodec.getCodecID(), jsonCodec);
    }

    /* Not supported in J2ME CLDC
    public static void useSameJsonCodecAs(Class javaClass1, Class javaClass2) {
        registerJsonCodec(javaClass1, getJsonEncoder(javaClass2));
    } */

    public static <J> JsonCodec<J> getJsonEncoder(Class<J> javaClass) {
        return encoders.get(javaClass);
        /* getSuperclass() is not supported in J2ME CLDC
        for (Class c = javaClass; c != null; c = c.getSuperclass()) {
            JsonCodec<J> jsonCodec = encoders.get(c);
            if (jsonCodec != null)
                return jsonCodec;
        }
        return null;
        */
    }

    public static <J> JsonCodec<J> getJsonDecoder(String jsonUID) {
        return decoders.get(jsonUID);
    }

    public static Object encodeToJson(Object object) {
        if (object == null || object instanceof String || Numbers.isNumber(object))
            return object;
        return encodeToJsonObject(object);
    }

    public static CompositeObject encodeToJsonObject(Object object) {
        if (object == null)
            return null;
        if (object instanceof CompositeObject)
            return (CompositeObject) object;
        WritableCompositeObject json = Json.createObject();
        encodeJavaObjectToJsonObject(object, json);
        return json;
    }

    public static void encodeJavaObjectToJsonObject(Object javaObject, WritableCompositeObject json) {
        JsonCodec encoder = getJsonEncoder(javaObject.getClass());
        if (encoder == null)
            throw new IllegalArgumentException("No JsonCodec for type: " + javaObject.getClass());
        json.set(CODEC_ID_KEY, encoder.getCodecID());
        encoder.encodeToJson(javaObject, json);
    }

    public static <J> J decodeFromJsonObject(CompositeObject json) {
        if (json == null)
            return null;
        String codecID = json.getString(CODEC_ID_KEY);
        JsonCodec<J> decoder = getJsonDecoder(codecID);
        if (decoder == null)
            throw new IllegalArgumentException("No JsonCodec for id: '" + codecID + "'");
        return decoder.decodeFromJson(json);
    }

    public static <J> J decodeFromJson(Object object) {
        if (object instanceof CompositeObject)
            return decodeFromJsonObject((CompositeObject) object);
        return (J) object;
    }

    public static CompositeArray encodePrimitiveArrayToJsonArray(Object[] primArray) {
        if (primArray == null)
            return null;
        WritableCompositeArray jsonArray = Json.createArray();
        for (Object value : primArray)
            jsonArray.push(value);
        return jsonArray;
    }

    public static Object[] decodePrimitiveArrayFromJsonArray(CompositeArray jsonArray) {
        if (jsonArray == null)
            return null;
        int n = jsonArray.size();
        Object[] array = new Object[n];
        for (int i = 0; i < n; i++)
            array[i] = jsonArray.getElement(i);
        return array;
    }

    public static CompositeArray encodeToJsonArray(Object[] array) {
        if (array == null)
            return null;
        WritableCompositeArray jsonArray = Json.createArray();
        for (Object object : array)
            jsonArray.push(encodeToJsonObject(object));
        return jsonArray;
    }

    public static <A> A[] decodeFromJsonArray(CompositeArray jsonArray, Class<A> expectedClass) {
        if (jsonArray == null)
            return null;
        int n = jsonArray.size();
        A[] array = (A[]) Array.newInstance(expectedClass, n);
        for (int i = 0; i < n; i++)
            array[i] = decodeFromJson(jsonArray.getObject(i));
        return array;
    }

    /****************************************************
     *            Json Codec registration               *
     * *************************************************/

    // Batch Json Serialization support

    private static String BATCH_CODEC_ID = "Batch";
    private static String BATCH_ARRAY_KEY = "array";

    static void registerBatchJsonCodec() {
        new AbstractJsonCodec<Batch>(Batch.class, BATCH_CODEC_ID) {

            @Override
            public void encodeToJson(Batch batch, WritableCompositeObject json) {
                json.set(BATCH_ARRAY_KEY, encodeToJsonArray(batch.getArray()));
            }

            @Override
            public Batch decodeFromJson(CompositeObject json) {
                return new Batch<>(decodeFromJsonArray(json.getArray(BATCH_ARRAY_KEY), SqlReadResult.class /* Temporary hardcoded TODO: guess the result class from the codecID */));
            }
        };
    }

    static {
        // Registering all required json codecs (especially for network bus calls)
        BusCallService.registerJsonCodecs();
        SqlArgument.registerJsonCodec();
        SqlReadResult.registerJsonCodec();
        registerBatchJsonCodec();
    }
}
