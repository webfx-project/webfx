package naga.core.util;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Bruno Salmon
 */
public class Strings {

    public static String asString(Object value) {
        return (String) value;
    }

    public static String toString(Object value) {
        return value == null ? null : value.toString();
    }

    public static String stringValue(Object value) {
        return value == null ? null : value.toString();
    }

    public static boolean isEmpty(String s) {
        return s == null || s.length() == 0;
    }

    public static boolean isNotEmpty(String s) {
        return !isEmpty(s);
    }

    public static boolean isEmpty(StringBuilder s) {
        return s == null || s.length() == 0;
    }

    public static boolean isNotEmpty(StringBuilder s) {
        return !isEmpty(s);
    }

    public static boolean startsWith(String s, String prefix) {
        return s != null && s.startsWith(prefix);
    }

    public static boolean endsWith(String s, String suffix) {
        return s != null && s.endsWith(suffix);
    }

    public static String removePrefix(String s, String prefix) {
        return startsWith(s, prefix) ? s.substring(prefix.length()) : s;
    }

    public static String removeSuffix(String s, String suffix) {
        return endsWith(s, suffix) ? s.substring(0, s.length() - suffix.length()) : s;
    }

    public static String replaceAll(String s, String match, String replacement) {
        if (s == null)
            return null;
        int pos = s.indexOf(match);
        if (pos == -1)
            return s;
        StringBuilder sb = new StringBuilder();
        int lastPos = 0;
        while (pos != -1) {
            sb.append(s.substring(lastPos, pos)).append(replacement);
            lastPos = pos + match.length();
            pos = s.indexOf(match, lastPos);
        }
        sb.append(s.substring(lastPos));
        return sb.toString();
    }

    public static String[] split(String s, String separator) {
        List<String> tokens = new ArrayList<String>();
        int p0 = 0;
        if (s != null)
            while (true) {
                int p1 = s.indexOf(separator, p0);
                if (p1 == -1) {
                    tokens.add(s.substring(p0));
                    break;
                }
                tokens.add(s.substring(p0, p1));
                p0 = p1 + separator.length();
            }
        return tokens.toArray(new String[tokens.size()]);
    }

    public static String concat(String... strings) {
        int length = strings.length;
        if (length == 0)
            return null;
        if (length == 1)
            return strings[0];
        StringBuffer sb = new StringBuffer();
        for (String s : strings)
            if (s != null)
                sb.append(s);
        return sb.toString();
    }

}
