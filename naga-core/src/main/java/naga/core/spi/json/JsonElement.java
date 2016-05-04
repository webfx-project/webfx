
package naga.core.spi.json;

import naga.core.valuesobject.WritableValuesElement;

/**
 * @author Bruno Salmon
 */
public interface JsonElement extends WritableValuesElement {

    /**
     * Returns a serialized JSON string representing this value.
     */
    String toJsonString();
}
