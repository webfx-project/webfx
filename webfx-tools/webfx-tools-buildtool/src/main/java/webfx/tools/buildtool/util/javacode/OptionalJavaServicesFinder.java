package webfx.tools.buildtool.util.javacode;

import java.util.regex.Pattern;

/**
 * @author Bruno Salmon
 */
public final class OptionalJavaServicesFinder extends JavaCodePatternFinder {

    private static JavaCodePattern SERVICE_PATTERN =
            new JavaCodePattern(Pattern.compile("^(?!.*SingleServiceProvider).*ServiceLoader\\s*\\.\\s*load\\s*\\(\\s*([a-z0-9A-Z.]+)\\.class", Pattern.MULTILINE), 1);

    public OptionalJavaServicesFinder(JavaCode javaCode) {
        super(SERVICE_PATTERN, javaCode);
    }

    @Override
    String mapFoundGroup(String group) {
        return resolveFullClassName(group);
    }
}
