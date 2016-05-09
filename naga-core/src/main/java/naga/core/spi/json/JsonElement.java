
package naga.core.spi.json;

import naga.core.composite.WritableCompositeElement;

/**
 * @author Bruno Salmon
 */
public interface JsonElement extends WritableCompositeElement {

    /**
     * Returns a serialized JSON string representing this value.
     */
    String toJsonString();
}
