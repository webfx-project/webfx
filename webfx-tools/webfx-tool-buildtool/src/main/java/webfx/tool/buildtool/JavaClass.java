package webfx.tool.buildtool;

import webfx.tool.buildtool.util.javacode.OptionalJavaServicesFinder;
import webfx.tool.buildtool.util.javacode.JavaCodePackagesFinder;
import webfx.tool.buildtool.util.javacode.RequiredJavaServicesFinder;
import webfx.tool.buildtool.util.reusablestream.ReusableStream;

import java.nio.file.Path;

/**
 * @author Bruno Salmon
 */
final class JavaClass {

    private final Path javaFilePath;
    private final ProjectModule projectModule;
    private String packageName;
    private String className;
    private final ReusableStream<String> usedJavaPackagesCache =
            ReusableStream.fromIterable(new JavaCodePackagesFinder(this::getJavaFilePath))
                    .distinct()
                    .cache();
    private final ReusableStream<String> usedRequiredJavaServicesCache =
            ReusableStream.fromIterable(new RequiredJavaServicesFinder(this::getJavaFilePath))
                    .distinct()
                    .cache();
    private final ReusableStream<String> usedOptionalJavaServicesCache =
            ReusableStream.fromIterable(new OptionalJavaServicesFinder(this::getJavaFilePath))
                    .distinct()
                    .cache();

    /***********************
     ***** Constructor *****
     ***********************/

    JavaClass(Path javaFilePath, ProjectModule projectModule) {
        this.javaFilePath = javaFilePath;
        this.projectModule = projectModule;
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

    ReusableStream<String> getUsedJavaPackages() {
        return usedJavaPackagesCache;
    }

    ReusableStream<String> getUsedRequiredJavaServices() {
        return usedRequiredJavaServicesCache;
    }

    ReusableStream<String> getUsedOptionalJavaServices() {
        return usedOptionalJavaServicesCache;
    }

    ReusableStream<String> getUsedJavaServices() {
        return getUsedRequiredJavaServices().concat(getUsedOptionalJavaServices());
    }


    /********************
     ***** Logging  *****
     ********************/

    @Override
    public String toString() {
        return getClassName();
    }

}
