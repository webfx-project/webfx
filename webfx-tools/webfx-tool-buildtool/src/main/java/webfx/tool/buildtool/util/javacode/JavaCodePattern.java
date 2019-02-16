package webfx.tool.buildtool.util.javacode;

import java.util.regex.Pattern;

/**
 * @author Bruno Salmon
 */
public final class JavaCodePattern {

    public static JavaCodePattern PACKAGE_PATTERN = new JavaCodePattern(Pattern.compile("([\\s(])([a-z]+(\\.[a-z]+)+)(\\.[A-Z*])"), 2);

    private final Pattern pattern;
    private final int matchingGroup;

    private JavaCodePattern(Pattern pattern, int matchingGroup) {
        this.pattern = pattern;
        this.matchingGroup = matchingGroup;
    }

    Pattern getPattern() {
        return pattern;
    }

    int getMatchingGroup() {
        return matchingGroup;
    }
}
