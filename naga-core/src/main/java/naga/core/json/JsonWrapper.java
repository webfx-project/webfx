package naga.core.json;

/**
 * @author Bruno Salmon
 */
public interface JsonWrapper {

    ElementType getNativeElementType(Object nativeElement);

    default <T> T anyNativeToComposite(Object nativeElement) {
        if (nativeElement == null)
            return null;
        switch (getNativeElementType(nativeElement)) {
            case STRING:
            case BOOLEAN:
            case NUMBER:
                return nativeToCompositeScalar(nativeElement);
            case ARRAY: return (T) nativeToCompositeArray(nativeElement);
            case OBJECT: return (T) nativeToCompositeObject(nativeElement);
        }
        return null;
    }

    default <T> T nativeToCompositeScalar(Object nativeScalar) { return (T) nativeScalar; }

    default WritableJsonArray nativeToCompositeArray(Object nativeArray) { return  (WritableJsonArray) nativeArray; }

    default WritableJsonObject nativeToCompositeObject(Object nativeObject) { return (WritableJsonObject) nativeObject; }

    default Object anyCompositeToNative(Object value) {
        if (value == null)
            return null;
        if (value instanceof JsonArray)
            return compositeToNativeArray((JsonArray) value);
        if (value instanceof JsonObject)
            return compositeToNativeObject((JsonObject) value);
        return compositeToNativeScalar(value);
    }

    default Object compositeToNativeScalar(Object scalar) { return scalar; }

    default Object compositeToNativeArray(JsonArray array) { return array.getNativeElement(); }

    default Object compositeToNativeObject(JsonObject object) { return object.getNativeElement(); }

}
