package webfx.tools.buildtool.util.javacode;

import java.util.regex.Pattern;

/**
 * @author Bruno Salmon
 */
final class JavaCodePattern {

    private final Pattern pattern;
    private final int matchingGroup;

    JavaCodePattern(Pattern pattern, int matchingGroup) {
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
