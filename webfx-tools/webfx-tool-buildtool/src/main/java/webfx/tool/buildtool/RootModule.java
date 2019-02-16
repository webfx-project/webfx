package webfx.tool.buildtool;

import java.nio.file.Path;
import java.util.*;
import java.util.stream.Stream;

/**
 * @author Bruno Salmon
 */
final class RootModule extends ProjectModule {

    private final Map<String /* package name */, Module> javaPackagesModules = new HashMap<>();
    private boolean allJavaClassesProjectModulesAreRegistered;

    /***********************
     ***** Constructor *****
     ***********************/

    RootModule(Path rootDirectoryPath) {
        super(rootDirectoryPath);
        registerThirdPartyModules();
    }


    /********************************
     ***** Registration methods *****
     ********************************/

    private void registerThirdPartyModules() {
        // JDK
        registerJavaPackageModule(new ModuleImpl("java.base"), "java.io", "java.lang", "java.lang.annotation", "java.lang.management", "java.lang.ref", "java.lang.reflect", "java.math", "java.net", "java.nio", "java.nio.charset", "java.nio.file", "java.nio.file.attribute", "java.security", "java.text", "java.time", "java.time.format", "java.time.temporal", "java.util", "java.util.logging", "java.util.function", "java.util.regex", "java.util.stream", "java.util.concurrent", "java.util.concurrent.atomic", "java.xml");
        registerJavaPackageModule(new ModuleImpl("java.sql"), "java.sql", "javax.sql");
        registerJavaPackageModule(new ModuleImpl("jdk.management"), "com.sun.management");
        registerJavaPackageModule(new ModuleImpl("jdk.jsobject"), "netscape.javascript");

        // JavaFx
        registerJavaPackageModule(new ModuleImpl("javafx.graphics"), "com.sun.prism", "javafx.concurrent");
        registerJavaPackageModule(new ModuleImpl("javafx.control"), "com.sun.javafx.scene.control.behavior", "javafx.scene.chart");
        registerJavaPackageModule(new ModuleImpl("javafx.web"), "javafx.scene.web");
        registerJavaPackageModule(new ModuleImpl("javafx.swing"), "javafx.embed.swing");

        // JavaFx SVG
        registerJavaPackageModule(new ModuleImpl("javafxsvg"), "de.codecentric.centerdevice.javafxsvg");

        // GWT
        registerJavaPackageModule(new ModuleImpl("gwt-user"), "com.google.gwt.user.client", "com.google.gwt.core.client", "com.google.gwt.resources.client", "com.google.gwt.regexp.shared", "com.google.gwt.storage.client");
        registerJavaPackageModule(new ModuleImpl("jsinterop-annotations"), "jsinterop.annotations");

        // GWT charts
        registerJavaPackageModule(new ModuleImpl("gwt-charts"), "com.googlecode.gwt.charts.client", "com.googlecode.gwt.charts.client.corechart");

        // Vert.x
        registerJavaPackageModule(new ModuleImpl("vertx-core"), "io.vertx.core", "io.vertx.core.eventbus", "io.vertx.core.http", "io.vertx.core.json", "io.vertx.core.net");
        registerJavaPackageModule(new ModuleImpl("vertx-web"), "io.vertx.ext.bridge", "io.vertx.ext.web", "io.vertx.ext.web.handler", "io.vertx.ext.web.handler.sockjs");
        registerJavaPackageModule(new ModuleImpl("vertx-sql-common"), "io.vertx.ext.sql");
        registerJavaPackageModule(new ModuleImpl("vertx-jdbc-client"), "io.vertx.ext.jdbc");
        registerJavaPackageModule(new ModuleImpl("vertx-mysql-postgresql-client"), "io.vertx.ext.asyncsql");

        // HikariCP
        registerJavaPackageModule(new ModuleImpl("HikariCP"), "com.zaxxer.hikari");
    }

    void registerJavaPackageModule(Module module, String... javaPackageNames) {
        for (String javaPackageName : javaPackageNames)
            registerJavaPackageModule(javaPackageName, module);
    }

    void registerJavaPackageModule(String javaPackageName, Module module) {
        Module m = javaPackagesModules.get(javaPackageName);
        if (m != null && m != module)
            warning(module + " and " + m + " share the same package " + javaPackageName);
        else
            javaPackagesModules.put(javaPackageName, module);
    }

    void registerJavaPackagesProjectModule(ProjectModule module) {
        module.getJavaClassesStream().forEach(javaClass -> registerJavaPackageModule(javaClass.getPackageName(), module));
    }

    private void registerAllJavaPackagesProjectModules() {
        getThisAndChildrenModulesInDepthStream().forEach(this::registerJavaPackagesProjectModule);
    }

    Module getJavaPackageNameModule(String javaPackageName) {
        checkAllJavaClassesProjectModulesAreRegistered();
        Module module = javaPackagesModules.get(javaPackageName);
        if (module == null)
            warning("Unknown module for package " + javaPackageName);
        return module;
    }

    private void checkAllJavaClassesProjectModulesAreRegistered() {
        if (!allJavaClassesProjectModulesAreRegistered) {
            registerAllJavaPackagesProjectModules();
            allJavaClassesProjectModulesAreRegistered = true;
        }
    }


    /*****************************
     ***** Analyzing streams *****
     *****************************/

    static Stream<Collection<Module>> analyzeDependenciesPathsBetween(Module sourceModule, Module destinationModule) {
        return analyzeDependenciesPathsBetween(new ArrayList<>(), sourceModule, destinationModule).stream();
    }

    private static Collection<Collection<Module>> analyzeDependenciesPathsBetween(Collection<Module> parentPath, Module sourceModule, Module destinationModule) {
        Collection<Collection<Module>> paths = new ArrayList<>();
        if (!parentPath.contains(sourceModule)) { // Skipping cyclic dependencies
            Collection<Module> extendedPath = extendModuleCollection(parentPath, sourceModule);
            if (destinationModule == sourceModule)
                paths.add(extendedPath);
            else if (sourceModule instanceof ProjectModule)
                ((ProjectModule) sourceModule).analyzeDirectDependencies()
                        .map(depModule -> analyzeDependenciesPathsBetween(extendedPath, depModule, destinationModule))
                        .forEach(paths::addAll);
        }
        return paths;
    }


    Stream<Collection<Module>> analyzeCyclicDependenciesPaths() {
        return getThisAndChildrenModulesInDepthStream()
                .flatMap(RootModule::analyzeCyclicDependenciesPaths)
                .distinct()
                ;
    }

    static Stream<Collection<Module>> analyzeCyclicDependenciesPaths(Module module) {
        return analyzeCyclicDependenciesPaths(new ArrayList<>(), module).stream().distinct();
    }

    private static List<Collection<Module>> analyzeCyclicDependenciesPaths(List<Module> parentPath, Module module) {
        List<Collection<Module>> paths = new ArrayList<>();
        int index = parentPath.indexOf(module);
        if (index != -1) { // Cyclic dependency found
            List<Module> cyclicPath = new ArrayList<>();
            while (index < parentPath.size())
                cyclicPath.add(parentPath.get(index++));
            cyclicPath.add(module);
            paths.add(cyclicPath);
        } else if (module instanceof ProjectModule) {
            List<Module> extendedPath = extendModuleCollection(parentPath, module);
            ((ProjectModule) module).analyzeDirectDependencies()
                    .map(depModule -> analyzeCyclicDependenciesPaths(extendedPath, depModule))
                    .forEach(paths::addAll);
        }
        return paths;
    }

    /***************************
     ***** Listing methods *****
     ***************************/

    //// Listing methods that are just forwarders to the target project module

    void listProjectModuleJavaClasses(String projectModule) {
        getChildModuleInDepth(projectModule).listJavaClasses();
    }

    void listProjectModuleJavaClassesDependingOn(String projectModule, String destinationModule) {
        getChildModuleInDepth(projectModule).listJavaClassesDependingOn(destinationModule);
    }

    void listProjectModuleDirectDependencies(String moduleArtifactId) {
        getChildModuleInDepth(moduleArtifactId).listDirectDependencies();
    }

    void listInDepthTransitiveDependencies(String moduleArtifactId) {
        getChildModuleInDepth(moduleArtifactId).listThisAndChildrenModulesInDepthTransitiveDependencies();
    }

    void listDependenciesPathsBetween(String sourceModule, String destinationModule) {
        listDependenciesPathsBetween(getChildModuleInDepth(sourceModule), getChildModuleInDepth(destinationModule));
    }

    private static void listDependenciesPathsBetween(Module sourceModule, Module destinationModule) {
        listStreamElements("Listing dependency paths between " + sourceModule + " and " + destinationModule,
                analyzeDependenciesPathsBetween(sourceModule, destinationModule)
        );
    }

    void listCyclicDependenciesPaths() {
        listStreamElements("Listing cyclic dependency paths",
                analyzeCyclicDependenciesPaths()
        );
    }


    /**********************************
     ***** Static utility methods *****
     **********************************/

    private static List<Module> extendModuleCollection(Collection<Module> parentPath, Module module) {
        List<Module> newCollection = new ArrayList<>(parentPath);
        newCollection.add(module);
        return newCollection;
    }
}
