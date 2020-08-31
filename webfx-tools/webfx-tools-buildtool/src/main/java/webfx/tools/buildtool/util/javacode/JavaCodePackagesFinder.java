package webfx.tools.buildtool.util.javacode;

import java.util.regex.Pattern;

/**
 * @author Bruno Salmon
 */
public final class JavaCodePackagesFinder extends JavaCodePatternFinder {

    /**
     * A regular expression is used to parse the java code and find the packages it uses such as the ones listed in the
     * imports section (ex: import a.b.c.MyClass => package = a.b.c) or those explicitly used in the java code (ex:
     * new java.util.ArrayList<>(); => package = java.util).
     *
     * It works for most of cases but it's not 100% accurate as it doesn't know about the java syntax (an improvement
     * would be to replace this by a java syntax analyzer) but just rely on conventional java naming which is that
     * only lowercase characters are used in a package as opposed to classes that start with a uppercase letter (an
     * exception is made for the lr_parser class coming from the javacup library).
     *
     */

    private final static JavaCodePattern PACKAGE_PATTERN =
            new JavaCodePattern(Pattern.compile("[\\s(](?!this\\.)([a-z_0-9]+(\\.[a-z_0-9]+)*)(\\.(\\*;|(lr_parser|[A-Z]+[0-9]*[a-z]+[A-Za-z_0-9]*)[\\s()<;.]))"), 1);

    public JavaCodePackagesFinder(JavaCode javaCode) {
        super(PACKAGE_PATTERN, javaCode);
    }
}
