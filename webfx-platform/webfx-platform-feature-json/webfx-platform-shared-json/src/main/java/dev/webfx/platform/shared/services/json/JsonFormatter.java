package dev.webfx.platform.shared.services.json;

import dev.webfx.platform.shared.util.Strings;

/**
 * @author Bruno Salmon
 */
public interface JsonFormatter extends JsonWrapper {

    Object getNativeElement();

    /**
     * Returns a serialized JSON string representing this value.
     */
    default String toJsonString() {
        return appendNativeElement(getNativeElement(), this, new StringBuilder()).toString();
    }

    /**
     * Make a JSON text of a JsonObject. For compactness, no whitespace
     * is added. If this would not result in a syntactically correct JSON text,
     * then null will be returned instead.
     * <p>
     * Warning: This method assumes that the data structure is acyclic.
     *
     * @return a printable, displayable, portable, transmittable
     *  representation of the object, beginning
     *  with <code>{</code>&nbsp;<small>(left brace)</small> and ending
     *  with <code>}</code>&nbsp;<small>(right brace)</small>.
     */

    static String toJsonString(JsonObject json) {
        return appendJsonObject(json, new StringBuilder()).toString();
    }

    static StringBuilder appendJsonObject(JsonObject json, StringBuilder sb) {
        sb.append('{');
        boolean first = true;
        JsonArray keys = json.keys();
        for (int i = 0, size = keys.size(); i < size; i++) {
            String key = keys.getString(i);
            if (!first)
                sb.append(',');
            appendQuoted(key, sb);
            sb.append(':');
            appendNativeElement(json.getNativeElement(key), json, sb);
            first = false;
        }
        return sb.append('}');
    }

    static String toJsonString(JsonArray ca) {
        return appendJsonArray(ca, new StringBuilder()).toString();
    }

    static StringBuilder appendJsonArray(JsonArray ca, StringBuilder sb) {
        return join(ca, ",", sb.append('[')).append(']');
    }
    /**
     * Make a string from the contents of this JSONArray. The
     * <code>separator</code> string is inserted between each element.
     * Warning: This method assumes that the data structure is acyclical.
     * @param separator A string that will be inserted between the elements.
     * @return a string.
     * @throws IllegalArgumentException If the array contains an invalid number.
     */
    static StringBuilder join(JsonArray ca, String separator, StringBuilder sb) {
        int len = ca.size();
        for (int i = 0; i < len; i += 1) {
            if (i > 0)
                sb.append(separator);
            appendNativeElement(ca.getNativeElement(i), ca, sb);
        }
        return sb;
    }

    /**
     * Make a JSON text of an Object element. If the object has an
     * element.toJSONString() method, then that method will be used to produce
     * the JSON text. The method is required to produce a strictly
     * conforming text. If the object does not contain a toJSONString
     * method (which is the most common case), then a text will be
     * produced by the rules.
     * <p>
     * Warning: This method assumes that the data structure is acyclical.
     * @param element The element to be serialized.
     * @return a printable, displayable, transmittable
     *  representation of the object, beginning
     *  with <code>{</code>&nbsp;<small>(left brace)</small> and ending
     *  with <code>}</code>&nbsp;<small>(right brace)</small>.
     * @throws IllegalArgumentException If the element is or contains an invalid number.
     */
    static StringBuilder appendNativeElement(Object element, JsonWrapper parent, StringBuilder sb) {
        switch (parent.getNativeElementType(element)) {
            case NULL:    return sb.append("null");
            case OBJECT:  return appendJsonObject(parent.nativeToJavaJsonObject(element), sb);
            case ARRAY:   return appendJsonArray(parent.nativeToJavaJsonArray(element), sb);
            case NUMBER:  return sb.append(numberToString(parent.nativeToJavaScalar(element)));
            case BOOLEAN: return sb.append((Boolean) parent.nativeToJavaScalar(element));
            case STRING:  return appendQuoted(parent.nativeToJavaScalar(element), sb);
            default:      return sb; // ignored when undefined
        }
    }

    /**
     * Shave off trailing zeros and decimal point, if possible.
     */
    static String trimNumber(String s) {
        if (s.indexOf('.') > 0 && s.indexOf('e') < 0 && s.indexOf('E') < 0) {
            while (s.endsWith("0"))
                s = s.substring(0, s.length() - 1);
            if (s.endsWith("."))
                s = s.substring(0, s.length() - 1);
        }
        return s;
    }

    /**
     * Produce a string from a Number.
     * @param  n A Number
     * @return A String.
     * @throws IllegalArgumentException If n is a non-finite number.
     */
    static String numberToString(Object n) throws IllegalArgumentException {
        if (n == null)
            throw new IllegalArgumentException("Null pointer");
        testValidity(n);
        return trimNumber(n.toString());
    }

    /**
     * Throw an exception if the object is an NaN or infinite number.
     * @param o The object to test.
     * @throws IllegalArgumentException If o is a non-finite number.
     */
    static void testValidity(Object o) throws IllegalArgumentException {
        if (o != null) {
            if (o instanceof Double) {
                if ( new Double(0.0).equals(o)){
                    // workaround for xmlvm bug that returns true to new Double(0.0).isInfinite()
                } else if (((Double)o).isInfinite() || ((Double)o).isNaN()) {
                    throw new IllegalArgumentException(
                            "JSON does not allow non-finite numbers");
                }
            } else if (o instanceof Float) {
                if ( new Float(0.0).equals(o) ){
                    // workaround for xmlvm bug that returns true to new Float(0.0).isInfinite()
                } else if (((Float)o).isInfinite() || ((Float)o).isNaN()) {
                    throw new IllegalArgumentException(
                            "JSON does not allow non-finite numbers.");
                }
            }
        }
    }

    /**
     * Produce a string in double quotes with backslash sequences in all the
     * right places. A backslash will be inserted within </, allowing JSON
     * text to be delivered in HTML. In JSON text, a string cannot contain a
     * control character or an unescaped quote or backslash.
     * @param string A String
     * @return  A String correctly formatted for insertion in a JSON text.
     */
    static StringBuilder appendQuoted(String string, StringBuilder sb) {
        if (Strings.isEmpty(string))
            return sb.append("\"\"");

        char         b;
        char         c = 0;
        int          i;
        int          len = string.length();
        String       t;

        sb.append('"');
        for (i = 0; i < len; i += 1) {
            b = c;
            c = string.charAt(i);
            switch (c) {
                case '\\':
                case '"':
                    sb.append('\\');
                    sb.append(c);
                    break;
                case '/':
                    if (b == '<') {
                        sb.append('\\');
                    }
                    sb.append(c);
                    break;
                case '\b':
                    sb.append("\\b");
                    break;
                case '\t':
                    sb.append("\\t");
                    break;
                case '\n':
                    sb.append("\\n");
                    break;
                case '\f':
                    sb.append("\\f");
                    break;
                case '\r':
                    sb.append("\\r");
                    break;
                default:
                    if (c < ' ') {
                        t = "000" + Integer.toHexString(c);
                        sb.append("\\u").append(t.substring(t.length() - 4));
                    } else
                        sb.append(c);
            }
        }
        return sb.append('"');
    }

}
