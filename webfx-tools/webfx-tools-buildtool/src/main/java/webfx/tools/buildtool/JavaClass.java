package webfx.tools.buildtool;

import webfx.tools.buildtool.util.javacode.JavaCode;
import webfx.tools.buildtool.util.javacode.OptionalJavaServicesFinder;
import webfx.tools.buildtool.util.javacode.JavaCodePackagesFinder;
import webfx.tools.buildtool.util.javacode.RequiredJavaServicesFinder;
import webfx.tools.util.reusablestream.ReusableStream;

import java.nio.file.Path;

/**
 * @author Bruno Salmon
 */
final class JavaClass {

    private final Path javaFilePath;
    private final ProjectModule projectModule;
    private final JavaCode javaCode = new JavaCode(this::getJavaFilePath);
    private String packageName;
    private String className;
    private final ReusableStream<String> usedJavaPackagesCache =
            ReusableStream.fromIterable(new JavaCodePackagesFinder(javaCode))
                    .distinct()
                    .cache();
    private final ReusableStream<String> usedRequiredJavaServicesCache =
            ReusableStream.fromIterable(new RequiredJavaServicesFinder(javaCode))
                    .distinct()
                    .cache();
    private final ReusableStream<String> usedOptionalJavaServicesCache =
            ReusableStream.fromIterable(new OptionalJavaServicesFinder(javaCode))
                    .distinct()
                    .cache();

    /***********************
     ***** Constructor *****
     ***********************/

    JavaClass(Path javaFilePath, ProjectModule projectModule) {
        this.javaFilePath = javaFilePath;
        this.projectModule = projectModule;
    }


    JavaCode getJavaCode() {
        return javaCode;
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
            int lastDotIndex = className.lastIndexOf('.');
            packageName = className.substring(0, lastDotIndex);
        }
        return packageName;
    }

    String getClassName() {
        if (className == null)
            className = javaFilePath.toString().substring(projectModule.getJavaSourceDirectory().toString().length() + 1, javaFilePath.toString().length() - 5).replaceAll("[/\\\\]", ".");
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

    boolean usesJavaClass(String javaClass) {
        return getJavaCode().getTextCode().contains(javaClass);
    }


    /********************
     ***** Logging  *****
     ********************/

    @Override
    public String toString() {
        return getClassName();
    }

}
