package naga.core.composite;

/**
 * @author Bruno Salmon
 */
public interface CompositeElement extends CompositesFactory {

    Object getNativeElement();

    /**
     * Return true if it is an array.
     */
    default boolean isArray() { return getNativeElementType(getNativeElement()) == ElementType.ARRAY; }

    /**
     * Return true if it is an object.
     */
    default boolean isObject() {return getNativeElementType(getNativeElement()) == ElementType.OBJECT; }

    /**
     * Length of the array or number of keys of the object
     */
    int size();

    /**
     * Make a copy of this object or array.
     */
    <SC extends CompositeElement> SC copy();

    /**
     * Returns a serialized JSON string representing this value.
     */
    String toJsonString();

}
