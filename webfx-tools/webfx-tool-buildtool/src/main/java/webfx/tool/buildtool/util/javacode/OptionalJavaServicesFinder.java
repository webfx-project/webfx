package webfx.tool.buildtool.util.javacode;

import java.nio.file.Path;
import java.util.function.Supplier;
import java.util.regex.Pattern;

/**
 * @author Bruno Salmon
 */
public final class OptionalJavaServicesFinder extends JavaCodePatternFinder {

    private static JavaCodePattern SERVICE_PATTERN =
            new JavaCodePattern(Pattern.compile("^(?!.*SingleServiceProvider).*ServiceLoader\\s*\\.\\s*load\\s*\\(\\s*([a-z0-9A-Z.]+)\\.class", Pattern.MULTILINE), 1);

    public OptionalJavaServicesFinder(Supplier<Path> javaPathSupplier) {
        super(SERVICE_PATTERN, javaPathSupplier);
    }

    public OptionalJavaServicesFinder(Path javaFilePath) {
        super(SERVICE_PATTERN, javaFilePath);
    }

    @Override
    String mapFoundGroup(String group) {
        return resolveFullClassName(group);
    }
}
