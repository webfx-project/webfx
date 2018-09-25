package webfx.platforms.core.services.json;

import webfx.platforms.core.util.noreflect.IndexedArray;
import webfx.platforms.core.util.noreflect.KeyObject;
import webfx.platforms.core.services.json.spi.JsonProvider;
import webfx.platforms.core.services.json.spi.impl.listmap.MapJsonObject;
import webfx.platforms.core.util.serviceloader.SingleServiceLoader;

/**
 * @author Bruno Salmon
 */
public final class Json {

    /***************************
     * Factory methods helpers *
     **************************/

    public static WritableJsonObject createObject() {
        return getProvider().createJsonObject();
    }

    public static <NO> WritableJsonObject createObject(NO nativeObject) {
        return getProvider().nativeToJavaJsonObject(nativeObject);
    }

    public static WritableJsonArray createArray() {
        return getProvider().createJsonArray();
    }

    public static <NA> WritableJsonArray createArray(NA nativeArray) {
        return getProvider().nativeToJavaJsonArray(nativeArray);
    }

    public static WritableJsonObject parseObject(String text) {
        return getProvider().parseObject(text);
    }

    public static WritableJsonArray parseArray(String text) {
        return getProvider().parseArray(text);
    }

    public static Object javaToNativeScalar(Object scalar) {
        return getProvider().javaToNativeScalar(scalar);
    }


    private static JsonProvider PROVIDER;

    public static void registerProvider(JsonProvider provider) {
        PROVIDER = provider;
    }

    public static JsonProvider getProvider() {
        if (PROVIDER == null) {
            registerProvider(SingleServiceLoader.loadService(JsonProvider.class, SingleServiceLoader.NotFoundPolicy.TRACE_AND_RETURN_NULL));
            if (PROVIDER == null) {
                System.out.println("Using default built-in JSON factory which is not interoperable with the underlying platform! Be sure you haven't forgot to call Json.registerProvider().");
                PROVIDER = new MapJsonObject();
            }
        }
        return PROVIDER;
    }

    /***********************************
     * Java conversion methods helpers *
     **********************************/

    public static <T> WritableJsonArray fromJavaArray(T[] javaArray) {
        if (javaArray == null)
            return null;
        WritableJsonArray valuesArray = createArray();
        for (Object javaValue : javaArray)
            valuesArray.push(javaValue);
        return valuesArray;
    }

    public static Object[] toJavaArray(JsonArray jsonArray) {
        if (jsonArray == null)
            return null;
        int length = jsonArray.size();
        Object[] javaArray = new Object[length];
        for (int i = 0; i < length; i++)
            javaArray[i] = jsonArray.getElement(i);
        return javaArray;
    }

    public static String toJsonString(Object nativeElement) {
        return JsonFormatter.appendNativeElement(nativeElement, Json.getProvider(), new StringBuilder()).toString();
    }

    public static WritableJsonObject mergeInto(JsonObject src, WritableJsonObject dst) {
        return mergeInto(src, dst, src.keys());
    }

    public static WritableJsonObject mergeInto(KeyObject src, WritableJsonObject dst, IndexedArray keys) {
        for (int i = 0, size = keys.size(); i < size; i++) {
            String key = keys.getString(i);
            Object value = src.get(key);
            dst.setNativeElement(key, value);
        }
        return dst;
    }
}
