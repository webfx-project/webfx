package webfx.tool.buildtool.util.javacode;

import java.nio.file.Path;
import java.util.function.Supplier;
import java.util.regex.Pattern;

/**
 * @author Bruno Salmon
 */
public final class RequiredJavaServicesFinder extends JavaCodePatternFinder {

    private static JavaCodePattern SERVICE_PATTERN =
            new JavaCodePattern(Pattern.compile("SingleServiceProvider\\s*\\.\\s*getProvider\\s*\\(\\s*([a-z0-9A-Z.]+)\\.class"), 1);

    public RequiredJavaServicesFinder(Supplier<Path> javaPathSupplier) {
        super(SERVICE_PATTERN, javaPathSupplier);
    }

    public RequiredJavaServicesFinder(Path javaFilePath) {
        super(SERVICE_PATTERN, javaFilePath);
    }

    @Override
    String mapFoundGroup(String group) {
        return resolveFullClassName(group);
    }
}
