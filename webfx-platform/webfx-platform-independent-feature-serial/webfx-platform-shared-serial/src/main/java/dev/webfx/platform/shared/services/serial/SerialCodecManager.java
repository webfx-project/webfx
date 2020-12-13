package dev.webfx.platform.shared.services.serial;

import dev.webfx.platform.shared.services.json.*;
import dev.webfx.platform.shared.services.serial.spi.SerialCodec;
import dev.webfx.platform.shared.services.serial.spi.impl.ExceptionSerialCodec;
import dev.webfx.platform.shared.util.Dates;
import dev.webfx.platform.shared.util.Numbers;

import java.lang.reflect.Array;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.Map;

/*
 * @author Bruno Salmon
 */

public final class SerialCodecManager {

    public static final String CODEC_ID_KEY = "$codec";

    private static final Map<Class, SerialCodec> encoders = new HashMap<>();
    private static final Map<String, SerialCodec> decoders = new HashMap<>();
    private static final Map<String, Class> javaClasses = new HashMap<>();

    static {
        registerSerialCodec(new ExceptionSerialCodec());
    }

    public static void registerSerialCodec(SerialCodec codec) {
        Class javaClass = codec.getJavaClass();
        encoders.put(javaClass, codec);
        decoders.put(codec.getCodecId(), codec);
        javaClasses.put(codec.getCodecId(), javaClass);
    }

    /* Not supported in J2ME CLDC
    public static void useSameJsonCodecAs(Class javaClass1, Class javaClass2) {
        registerJsonCodec(javaClass1, getJsonEncoder(javaClass2));
    } */

    public static <J> SerialCodec<J> getSerialEncoder(Class<J> javaClass) {
        for (Class c = javaClass; c != null; c = c.getSuperclass()) {
            SerialCodec<J> codec = encoders.get(c);
            if (codec != null)
                return codec;
        }
        return null;
    }

    public static SerialCodec getSerialDecoder(String codecId) {
        return decoders.get(codecId);
    }

    public static Class getJavaClass(String codecId) {
        return javaClasses.get(codecId);
    }

    public static Object encodeToJson(Object object) {
        // Keeping null and primitives as is
        if (object == null || object instanceof String || Numbers.isNumber(object) || object instanceof Boolean)
            return object;
        // Managing date objects (Instant, LocalDate and LocalDateTime)
        Instant instant = Dates.asInstant(object);
        if (instant == null) {
            LocalDateTime localDateTime = Dates.asLocalDateTime(object);
            if (localDateTime == null)
                localDateTime = Dates.toLocalDateTime(Dates.asLocalDate(object));
            if (localDateTime != null)
                instant = localDateTime.toInstant(ZoneOffset.UTC);
        }
        if (instant != null)
            return Dates.formatIso(instant);
        // Other java objects are serialized into json
        return encodeToJsonObject(object);
    }

    public static JsonObject encodeToJsonObject(Object object) {
        if (object == null)
            return null;
        if (object instanceof JsonObject)
            return (JsonObject) object;
        return encodeJavaObjectToJsonObject(object, Json.createObject());
    }

    private static WritableJsonObject encodeJavaObjectToJsonObject(Object javaObject, WritableJsonObject json) {
        SerialCodec encoder = getSerialEncoder(javaObject.getClass());
        if (encoder == null)
            throw new IllegalArgumentException("No SerialCodec for type: " + javaObject.getClass());
        json.set(CODEC_ID_KEY, encoder.getCodecId());
        encoder.encodeToJson(javaObject, json);
        return json;
    }

    public static <J> J decodeFromJsonObject(JsonObject json) {
        if (json == null)
            return null;
        String codecId = json.getString(CODEC_ID_KEY);
        SerialCodec<J> decoder = getSerialDecoder(codecId);
        if (decoder == null)
            throw new IllegalArgumentException("No SerialCodec found for id: '" + codecId + "' when trying to decode " + json.toJsonString());
        return decoder.decodeFromJson(json);
    }

    public static <J> J decodeFromJson(Object object) {
        if (object instanceof JsonObject)
            return decodeFromJsonObject((JsonObject) object);
        object = Dates.fastToInstantIfIsoString(object);
        return (J) object;
    }

    public static JsonArray encodePrimitiveArrayToJsonArray(Object[] primArray) {
        if (primArray == null)
            return null;
        WritableJsonArray ca = Json.createArray();
        for (Object value : primArray)
            ca.push(encodeToJson(value));
        return ca;
    }

    public static Object[] decodePrimitiveArrayFromJsonArray(JsonArray ca) {
        if (ca == null)
            return null;
        int n = ca.size();
        Object[] array = new Object[n];
        for (int i = 0; i < n; i++)
            array[i] = decodeFromJson(ca.getElement(i));
        return array;
    }

    public static JsonArray encodeToJsonArray(Object[] array) {
        if (array == null)
            return null;
        WritableJsonArray ca = Json.createArray();
        for (Object object : array)
            ca.push(encodeToJsonObject(object));
        return ca;
    }

    public static <A> A[] decodeFromJsonArray(JsonArray ca, Class<A> expectedClass) {
        if (ca == null)
            return null;
        int n = ca.size();
        A[] array = (A[]) Array.newInstance(expectedClass, n);
        for (int i = 0; i < n; i++)
            array[i] = decodeFromJson(ca.getObject(i));
        return array;
    }
}
