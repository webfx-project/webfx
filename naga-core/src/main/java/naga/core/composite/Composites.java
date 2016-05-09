package naga.core.composite;

/**
 * @author Bruno Salmon
 */
public class Composites {

    /***************************
     * Factory methods helpers *
     **************************/

    public static WritableCompositeObject createObject() {
        return getFactory().createCompositeObject();
    }

    public static <NO> WritableCompositeObject createObject(NO nativeObject) {
        return getFactory().nativeToCompositeObject(nativeObject);
    }

    public static WritableCompositeArray createArray() {
        return getFactory().createCompositeArray();
    }

    public static <NA> WritableCompositeArray createArray(NA nativeArray) {
        return getFactory().nativeToCompositeArray(nativeArray);
    }

    public static WritableCompositeObject parseObject(String text) {
        return getFactory().parseObject(text);
    }

    public static WritableCompositeArray parseArray(String text) {
        return getFactory().parseArray(text);
    }

    private static CompositesFactory FACTORY;

    public static void registerFactory(CompositesFactory factory) {
        FACTORY = factory;
    }

    public static CompositesFactory getFactory() {
        /*if (FACTORY == null) {
            Platform platform = Platform.get();// Getting the platform should load the service provider and register the platform which should set the JSON factory
            if (platform != null)
                registerFactory(platform.jsonFactory());
            if (FACTORY == null)
                throw new IllegalStateException("You must register a JSON factory first by invoking Json.registerFactory()");
        }*/
        return FACTORY;
    }

    /***********************************
     * Java conversion methods helpers *
     **********************************/

    public static <T> WritableCompositeArray fromJavaArray(T[] javaArray) {
        WritableCompositeArray valuesArray = createArray();
        for (Object javaValue : javaArray)
            valuesArray.push(javaValue);
        return valuesArray;
    }

    public static Object[] toJavaArray(CompositeArray jsonArray) {
        int length = jsonArray.size();
        Object[] javaArray = new Object[length];
        for (int i = 0; i < length; i++)
            javaArray[i] = jsonArray.getElement(i);
        return javaArray;
    }

}
