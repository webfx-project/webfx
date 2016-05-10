package naga.core.composite.listmap;

import naga.core.composite.CompositeArray;
import naga.core.composite.CompositeObject;
import naga.core.composite.ElementType;
import naga.core.composite.WritableCompositeElement;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Bruno Salmon
 */
public interface ListMapCompositeElement extends WritableCompositeElement {

    @Override
    default Object createNativeObject() {
        return new LinkedHashMap<>();
    }

    @Override
    default Object createNativeArray() {
        return new ArrayList();
    }

    @Override
    default Object parseNativeObject(String text) {
        throw new UnsupportedOperationException();
    }

    @Override
    default Object parseNativeArray(String text) {
        throw new UnsupportedOperationException();
    }

    @Override
    default ElementType getNativeElementType(Object nativeElement) {
        if (nativeElement instanceof Map || nativeElement instanceof CompositeObject)
            return ElementType.OBJECT;
        if (nativeElement instanceof List || nativeElement instanceof CompositeArray)
            return ElementType.ARRAY;
        return ElementType.SCALAR;
    }

}
