package naga.core.composite;

/**
 * @author Bruno Salmon
 */
public interface CompositesParser extends CompositesWrapper {

    /**
     * Parse a text into a native object.
     * @param text the text to parse
     * @return the native object
     */
    Object parseNativeObject(String text);

    /**
     * Parse a text into a native array.
     * @param text the text to parse
     * @return the native array
     */
    Object parseNativeArray(String text);

    /**
     * Parse a text into a composite object.
     * @param text the text to parse
     * @return the composite object
     */
    default WritableCompositeObject parseObject(String text) {
        return nativeToCompositeObject(parseNativeObject(text));
    }

    /**
     * Parse a text into a composite array.
     * @param text the text to parse
     * @return the composite array
     */
    default WritableCompositeArray parseArray(String text) {
        return nativeToCompositeArray(parseNativeArray(text));
    }
}
