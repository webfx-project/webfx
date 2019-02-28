package webfx.tool.buildtool.util.javacode;

import java.nio.file.Path;
import java.util.function.Supplier;
import java.util.regex.Pattern;

/**
 * @author Bruno Salmon
 */
public final class JavaCodePackagesFinder extends JavaCodePatternFinder {

    private final static JavaCodePattern PACKAGE_PATTERN =
            new JavaCodePattern(Pattern.compile("[\\s(]([a-z_0-9]+(\\.[a-z_0-9]+)*)(\\.(\\*;|(lr_parser|[A-Z][A-Za-z_0-9]*)[\\s()<;.]))"), 1);

    public JavaCodePackagesFinder(Supplier<Path> javaPathSupplier) {
        super(PACKAGE_PATTERN, javaPathSupplier);
    }

    public JavaCodePackagesFinder(Path javaFilePath) {
        super(PACKAGE_PATTERN, javaFilePath);
    }
}
