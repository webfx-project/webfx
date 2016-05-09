package naga.core.spi.json.cn1;

import naga.core.spi.json.JsonArray;
import naga.core.spi.json.listmap.ListCompositeArray;

import java.util.List;

/**
 * @author Bruno Salmon
 */
final class Cn1JsonArray extends ListCompositeArray implements Cn1JsonElement, JsonArray {

    public Cn1JsonArray() {}

    public Cn1JsonArray(List list) {
        super(list);
    }

    /**
     * Make a JSON text of this JSONArray. For compactness, no
     * unnecessary whitespace is added. If it is not possible to produce a
     * syntactically correct JSON text then null will be returned instead. This
     * could occur if the array contains an invalid number.
     * <p>
     * Warning: This method assumes that the data structure is acyclical.
     *
     * @return a printable, displayable, transmittable
     *  representation of the array.
     */
    @Override
    public String toJsonString() {
        return toJsonString(getNativeElement());
    }

    static String toJsonString(List list) {
        try {
            return '[' + join(list, ",") + ']';
        } catch (Exception e) {
            return null;
        }
    }
    /**
     * Make a string from the contents of this JSONArray. The
     * <code>separator</code> string is inserted between each element.
     * Warning: This method assumes that the data structure is acyclical.
     * @param separator A string that will be inserted between the elements.
     * @return a string.
     * @throws IllegalArgumentException If the array contains an invalid number.
     */
    static String join(List list, String separator) throws IllegalArgumentException {
        int len = list.size();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < len; i += 1) {
            if (i > 0)
                sb.append(separator);
            sb.append(Cn1JsonObject.valueToString(list.get(i)));
        }
        return sb.toString();
    }

}
