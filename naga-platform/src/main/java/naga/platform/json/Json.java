package naga.platform.json;

import naga.platform.json.listmap.MapJsonObject;
import naga.platform.json.spi.JsonArray;
import naga.platform.json.spi.JsonFactory;
import naga.platform.json.spi.WritableJsonArray;
import naga.platform.json.spi.WritableJsonObject;

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

    public static Object javaToNativeScalar(Object scalar) {
        return getFactory().javaToNativeScalar(scalar);
    }


    private static JsonFactory FACTORY;

    public static void registerFactory(JsonFactory factory) {
        FACTORY = factory;
    }

    public static JsonFactory getFactory() {
        if (FACTORY == null) {
            System.out.println("Using default built-in JSON factory which is not interoperable with the underlying platform! Be sure you haven't forget to call Json.registerFactory().");
            FACTORY = new MapJsonObject();
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
