package webfx.tool.buildtool.util.javacode;

import java.nio.file.Path;
import java.util.function.Supplier;
import java.util.regex.Pattern;

/**
 * @author Bruno Salmon
 */
public final class JavaCodePackagesFinder extends JavaCodePatternFinder {

    private final static JavaCodePattern PACKAGE_PATTERN =
            new JavaCodePattern(Pattern.compile("[\\s(]([a-z0-9]+(\\.[a-z0-9]+)*)(\\.[A-Z*])"), 1);

    public JavaCodePackagesFinder(Supplier<Path> javaPathSupplier) {
        super(PACKAGE_PATTERN, javaPathSupplier);
    }

    public JavaCodePackagesFinder(Path javaFilePath) {
        super(PACKAGE_PATTERN, javaFilePath);
    }
}
