package naga.core.composite;

/**
 * @author Bruno Salmon
 */
public interface CompositesWrapper {

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

    default WritableCompositeArray nativeToCompositeArray(Object nativeArray) { return  (WritableCompositeArray) nativeArray; }

    default WritableCompositeObject nativeToCompositeObject(Object nativeObject) { return (WritableCompositeObject) nativeObject; }

    default Object anyCompositeToNative(Object value) {
        if (value == null)
            return null;
        if (value instanceof CompositeArray)
            return compositeToNativeArray((CompositeArray) value);
        if (value instanceof CompositeObject)
            return compositeToNativeObject((CompositeObject) value);
        return compositeToNativeScalar(value);
    }

    default Object compositeToNativeScalar(Object scalar) { return scalar; }

    default Object compositeToNativeArray(CompositeArray array) { return array.getNativeElement(); }

    default Object compositeToNativeObject(CompositeObject object) { return object.getNativeElement(); }

}
