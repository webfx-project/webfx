package webfx.tool.buildtool.util.javacode;

import java.util.regex.Pattern;

/**
 * @author Bruno Salmon
 */
public final class JavaCodePackagesFinder extends JavaCodePatternFinder {

    private final static JavaCodePattern PACKAGE_PATTERN =
            new JavaCodePattern(Pattern.compile("[\\s(](?!this\\.)([a-z_0-9]+(\\.[a-z_0-9]+)*)(\\.(\\*;|(lr_parser|[A-Z][A-Za-z_0-9]*)[\\s()<;.]))"), 1);

    public JavaCodePackagesFinder(JavaCode javaCode) {
        super(PACKAGE_PATTERN, javaCode);
    }
}
