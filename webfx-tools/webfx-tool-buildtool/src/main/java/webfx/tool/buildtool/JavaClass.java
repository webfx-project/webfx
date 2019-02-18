package webfx.tool.buildtool;

import webfx.tool.buildtool.util.javacode.JavaCodePattern;
import webfx.tool.buildtool.util.javacode.JavaCodePatternFinder;
import webfx.tool.buildtool.util.streamable.Streamable;

import java.nio.file.Path;
import java.util.stream.Stream;

/**
 * @author Bruno Salmon
 */
final class JavaClass {

    private final Path javaFilePath;
    private final ProjectModule projectModule;
    private String packageName;
    private String className;
    private final Streamable<String> usedJavaPackagesNamesCache;

    /***********************
     ***** Constructor *****
     ***********************/

    JavaClass(Path javaFilePath, ProjectModule projectModule) {
        this.javaFilePath = javaFilePath;
        this.projectModule = projectModule;
        // Cache is instantiated now (because declared final)
        usedJavaPackagesNamesCache = Streamable.fromIterable(new JavaCodePatternFinder(JavaCodePattern.PACKAGE_PATTERN, javaFilePath))
                .distinct()
                .cache();
    }


    /*************************
     ***** Basic getters *****
     *************************/

    Path getJavaFilePath() {
        return javaFilePath;
    }

    ProjectModule getProjectModule() {
        return projectModule;
    }

    String getPackageName() {
        if (packageName == null) {
            getClassName();
            packageName = className.substring(0, className.lastIndexOf('.'));
        }
        return packageName;
    }

    String getClassName() {
        if (className == null)
            className = javaFilePath.toString().substring(projectModule.getJavaSourceDirectoryPath().toString().length() + 1, javaFilePath.toString().length() - 5).replaceAll("[/\\\\]", ".");
        return className;
    }


    /******************************
     ***** Analyzing streams  *****
     ******************************/

    Streamable<String> getUsedJavaPackagesNamesCache() {
        return usedJavaPackagesNamesCache;
    }

    Stream<String> analyzeUsedJavaPackagesNames() {
        return usedJavaPackagesNamesCache.stream();
    }


    /********************
     ***** Logging  *****
     ********************/

    @Override
    public String toString() {
        return getClassName();
    }

}
