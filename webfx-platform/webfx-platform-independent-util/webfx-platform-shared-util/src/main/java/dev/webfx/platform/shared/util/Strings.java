package dev.webfx.platform.shared.util;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Bruno Salmon
 */
public final class Strings {

    public static String asString(Object value) {
        return value instanceof String ? (String) value : null;
    }

    public static String toString(Object value) {
        return value == null ? null : value.toString();
    }

    public static String toSafeString(Object value) {
        return value == null ? "" : value.toString();
    }

    public static String stringValue(Object value) {
        return value == null ? null : value.toString();
    }

    public static int length(CharSequence s) {
        return s == null ? 0 : s.length();
    }

    public static boolean isEmpty(CharSequence s) {
        return length(s) == 0;
    }

    public static boolean isNotEmpty(CharSequence s) {
        return !isEmpty(s);
    }

    public static String trim(String s) {
        return s == null ? null : s.trim();
    }

    public static boolean contains(String s, String what) {
        return s != null && s.contains(what);
    }

    public static boolean startsWith(String s, String prefix) {
        return s != null && prefix != null && s.startsWith(prefix);
    }

    public static boolean endsWith(String s, String suffix) {
        return s != null && suffix != null && s.endsWith(suffix);
    }

    public static String removePrefix(String s, String prefix) {
        return startsWith(s, prefix) ? s.substring(prefix.length()) : s;
    }

    public static String removeSuffix(String s, String suffix) {
        return endsWith(s, suffix) ? s.substring(0, s.length() - suffix.length()) : s;
    }

    public static String replaceAllSafe(String s, String match, Object replacement) {
        return replaceAll(s, match, toSafeString(replacement));
    }

    public static String replaceAll(String s, String match, Object replacement) {
        if (s == null)
            return null;
        int pos = s.indexOf(match);
        if (pos == -1)
            return s;
        StringBuilder sb = new StringBuilder();
        int lastPos = 0;
        while (pos != -1) {
            sb.append(s, lastPos, pos).append(replacement);
            lastPos = pos + match.length();
            pos = s.indexOf(match, lastPos);
        }
        sb.append(s.substring(lastPos));
        return sb.toString();
    }

    public static String[] split(String s, String separator) {
        List<String> tokens = new ArrayList<>();
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
        StringBuilder sb = new StringBuilder();
        for (String s : strings)
            if (s != null)
                sb.append(s);
        return sb.toString();
    }

    public static String appendToken(String s, String token, String separator) {
        if (isEmpty(s))
            return token;
        if (isEmpty(token))
            return s;
        return s + separator + token;
    }

}
