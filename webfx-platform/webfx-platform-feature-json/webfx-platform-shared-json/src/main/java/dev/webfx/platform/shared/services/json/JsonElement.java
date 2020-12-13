package dev.webfx.platform.shared.services.json;

import dev.webfx.platform.shared.services.json.spi.JsonProvider;

/**
 * @author Bruno Salmon
 */
public interface JsonElement extends JsonProvider {

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
    default <SC extends JsonElement> SC copy() {
        return isArray() ? (SC) parseArray(toJsonString()) : (SC) parseObject(toJsonString());
    }

}
