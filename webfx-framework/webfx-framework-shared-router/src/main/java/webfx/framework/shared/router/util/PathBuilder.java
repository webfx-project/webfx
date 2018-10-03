package webfx.framework.shared.router.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Bruno Salmon
 */
public final class PathBuilder {

    private final static Pattern PATH_WITH_SEMI_COLON_PARAMETER_PATTERN = Pattern.compile(".*(:([^/|)]+)).*");

    public static String toRegexPath(String path) {
        while (true) {
            Matcher matcher = PATH_WITH_SEMI_COLON_PARAMETER_PATTERN.matcher(path);
            if (!matcher.matches())
                break;
            path = path.replace(matcher.group(1),"(?<" + matcher.group(2) + ">[^/]*)");
        }
        return path;
    }

    private StringBuilder sb = new StringBuilder();
    private boolean regex;
    private String path;

    public PathBuilder() {
    }

    public PathBuilder(String path) {
        appendPath(path);
    }

    public boolean isRegex() {
        return regex;
    }

    public String getPath() {
        return path;
    }

    private PathBuilder toRegex() {
        if (!regex)
            sb = new StringBuilder(toRegexPath(sb.toString(), regex = true));
        return this;
    }

    private PathBuilder append(String trusted) {
        sb.append(trusted);
        return this;
    }

    private PathBuilder appendCheckSlashPrefixed(String trusted) {
        if (!trusted.startsWith("/"))
            sb.append("/");
        return append(trusted);
    }

    public PathBuilder appendPath(String path) {
        return appendCheckSlashPrefixed(toRegexPath(path, regex));
    }

    public PathBuilder appendRegexPath(String regexPath) {
        return toRegex().appendCheckSlashPrefixed(regexPath);
    }

    public PathBuilder appendParameter(String path, String parameterName) {
        return appendParameter(path, parameterName, false);
    }

    public PathBuilder appendOptionalParameter(String path, String parameterName) {
        return appendParameter(path, parameterName, true);
    }

    public PathBuilder appendParameter(String path, String parameterName, boolean optional) {
        toRegex();
        if (optional)
            sb.append('(');
        appendPath(path).appendParameter(parameterName);
        if (optional)
            sb.append(")?");
        return this;
    }

    public PathBuilder appendParameter(String parameterName) {
        return toRegex().appendCheckSlashPrefixed("(?<").append(parameterName).append(">[^/]*)");
    }

    private static String toRegexPath(String path, boolean regex) {
        return regex ? toRegexPath(path) : path;
    }

    public String buildPath() {
        if (path == null) {
            path = sb.toString();
            sb = null;
        }
        return path;
    }
}
