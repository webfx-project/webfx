package naga.core.spi.json.cn1;

import naga.core.spi.json.JsonElement;
import naga.core.spi.json.JsonObject;
import naga.core.spi.json.listmap.MapCompositeObject;
import naga.core.util.Numbers;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author Bruno Salmon
 */
final class Cn1JsonObject extends MapCompositeObject implements Cn1JsonElement, JsonObject {

    public Cn1JsonObject() {
    }

    public Cn1JsonObject(Map map) {
        super(map);
    }

    /**
     * Make a JSON text of this JSONObject. For compactness, no whitespace
     * is added. If this would not result in a syntactically correct JSON text,
     * then null will be returned instead.
     * <p>
     * Warning: This method assumes that the data structure is acyclical.
     *
     * @return a printable, displayable, portable, transmittable
     *  representation of the object, beginning
     *  with <code>{</code>&nbsp;<small>(left brace)</small> and ending
     *  with <code>}</code>&nbsp;<small>(right brace)</small>.
     */
    @Override
    public String toJsonString() {
        return toJsonString(getNativeElement());
    }

    static String toJsonString(Map<String, Object> map) {
        try {
            Iterator keys = map.keySet().iterator();
            StringBuilder sb = new StringBuilder("{");

            while (keys.hasNext()) {
                if (sb.length() > 1)
                    sb.append(',');
                Object o = keys.next();
                sb.append(quote(o.toString()));
                sb.append(':');
                sb.append(valueToString(map.get(o)));
            }
            sb.append('}');
            return sb.toString();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Make a JSON text of an Object value. If the object has an
     * value.toJSONString() method, then that method will be used to produce
     * the JSON text. The method is required to produce a strictly
     * conforming text. If the object does not contain a toJSONString
     * method (which is the most common case), then a text will be
     * produced by the rules.
     * <p>
     * Warning: This method assumes that the data structure is acyclical.
     * @param value The value to be serialized.
     * @return a printable, displayable, transmittable
     *  representation of the object, beginning
     *  with <code>{</code>&nbsp;<small>(left brace)</small> and ending
     *  with <code>}</code>&nbsp;<small>(right brace)</small>.
     * @throws IllegalArgumentException If the value is or contains an invalid number.
     */
    static String valueToString(Object value) throws IllegalArgumentException {
        if (value == null )
            return "null";
        if (value instanceof String)
            return quote((String)value);
        if (value instanceof JsonElement)
            return ((JsonElement) value).toJsonString();
        if (Numbers.isNumber(value))
            return numberToString(value);
        if (value instanceof Boolean)
            return value.toString();
        if (value instanceof Map)
            return toJsonString((Map) value);
        if (value instanceof List)
            return Cn1JsonArray.toJsonString((List) value);
        return quote(value.toString());
    }

    /**
     * Shave off trailing zeros and decimal point, if possible.
     */
    static public String trimNumber(String s) {
        if (s.indexOf('.') > 0 && s.indexOf('e') < 0 && s.indexOf('E') < 0) {
            while (s.endsWith("0")) {
                s = s.substring(0, s.length() - 1);
            }
            if (s.endsWith(".")) {
                s = s.substring(0, s.length() - 1);
            }
        }
        return s;
    }

    /**
     * Produce a string from a Number.
     * @param  n A Number
     * @return A String.
     * @throws IllegalArgumentException If n is a non-finite number.
     */
    static public String numberToString(Object n) throws IllegalArgumentException {
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
    public static String quote(String string) {
        if (string == null || string.length() == 0) {
            return "\"\"";
        }

        char         b;
        char         c = 0;
        int          i;
        int          len = string.length();
        StringBuilder sb = new StringBuilder(len + 4);
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
                        sb.append("\\u" + t.substring(t.length() - 4));
                    } else {
                        sb.append(c);
                    }
            }
        }
        sb.append('"');
        return sb.toString();
    }
}
