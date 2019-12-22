package webfx.tools.buildtool.util.javacode;

import java.util.regex.Pattern;

/**
 * @author Bruno Salmon
 */
public final class RequiredJavaServicesFinder extends JavaCodePatternFinder {

    private static JavaCodePattern SERVICE_PATTERN =
            new JavaCodePattern(Pattern.compile("SingleServiceProvider\\s*\\.\\s*getProvider\\s*\\(\\s*([a-z0-9A-Z.]+)\\.class"), 1);

    public RequiredJavaServicesFinder(JavaCode javaCode) {
        super(SERVICE_PATTERN, javaCode);
    }

    @Override
    String mapFoundGroup(String group) {
        return resolveFullClassName(group);
    }
}
