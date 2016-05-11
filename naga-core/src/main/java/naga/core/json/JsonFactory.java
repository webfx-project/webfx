package naga.core.json;

/**
 * @author Bruno Salmon
 */
public interface JsonFactory extends JsonParser, JsonFormatter {

    /**
     * Create an empty native object.
     * @return a new empty native object
     */
    Object createNativeObject();

    /**
     * Create an empty native array.
     * @return a new empty native array
     */
    Object createNativeArray();

    /**
     * Create an empty json object.
     * @return a new empty json object
     */
    default WritableJsonObject createJsonObject() {
        return nativeToCompositeObject(createNativeObject());
    }

    /**
     * Create an empty json array.
     * @return a new empty json array
     */
    default WritableJsonArray createJsonArray() {
        return nativeToCompositeArray(createNativeArray());
    }

}
