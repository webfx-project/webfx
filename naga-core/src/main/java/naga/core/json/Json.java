package naga.core.json;

import naga.core.spi.platform.Platform;

/**
 * @author Bruno Salmon
 */
public class Json {

    /***************************
     * Factory methods helpers *
     **************************/

    public static WritableJsonObject createObject() {
        return getFactory().createJsonObject();
    }

    public static <NO> WritableJsonObject createObject(NO nativeObject) {
        return getFactory().nativeToJavaJsonObject(nativeObject);
    }

    public static WritableJsonArray createArray() {
        return getFactory().createJsonArray();
    }

    public static <NA> WritableJsonArray createArray(NA nativeArray) {
        return getFactory().nativeToJavaJsonArray(nativeArray);
    }

    public static WritableJsonObject parseObject(String text) {
        return getFactory().parseObject(text);
    }

    public static WritableJsonArray parseArray(String text) {
        return getFactory().parseArray(text);
    }

    private static JsonFactory FACTORY;

    public static void registerFactory(JsonFactory factory) {
        FACTORY = factory;
    }

    public static JsonFactory getFactory() {
        if (FACTORY == null) {
            Platform platform = Platform.get();// Getting the platform should load the service provider and register the platform which should set the JSON factory
            if (platform != null)
                registerFactory(platform.jsonFactory());
            if (FACTORY == null)
                throw new IllegalStateException("You must register a JSON factory first by invoking Json.registerFactory()");
        }
        return FACTORY;
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

}
