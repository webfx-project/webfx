package webfx.tool.buildtool;

import webfx.tool.buildtool.util.reusablestream.ReusableStream;

import java.nio.file.Path;
import java.util.*;

/**
 * @author Bruno Salmon
 */
final class RootModule extends ProjectModule {

    private final Map<String /* package name */, Module> javaPackagesModules = new HashMap<>();
    private final ReusableStream<ProjectModule> thisAndChildrenModulesInDepthResume =
            getThisAndChildrenModulesInDepth()
                    .resume();
    private final ReusableStream<Collection<Module>> cyclicDependencyLoopsCache =
            getThisAndChildrenModulesInDepth()
                    .flatMap(RootModule::analyzeCyclicDependenciesLoops)
                    .distinct()
                    .cache();

    /***********************
     ***** Constructor *****
     ***********************/

    RootModule(Path rootDirectory) {
        super(rootDirectory);
        registerThirdPartyModules();
    }


    /********************************
     ***** Registration methods *****
     ********************************/

    private void registerThirdPartyModules() {
        // JDK
        registerJavaPackageModule(Module.create("java.base"), "java.io", "java.lang", "java.lang.annotation", "java.lang.management", "java.lang.ref", "java.lang.reflect", "java.math", "java.net", "java.nio", "java.nio.charset", "java.nio.file", "java.nio.file.attribute", "java.security", "java.text", "java.time", "java.time.format", "java.time.temporal", "java.util", "java.util.function", "java.util.regex", "java.util.stream", "java.util.concurrent", "java.util.concurrent.atomic");
        registerJavaPackageModule(Module.create("java.xml"), "javax.xml");
        registerJavaPackageModule(Module.create("java.sql"), "java.sql", "javax.sql");
        registerJavaPackageModule(Module.create("java.logging"), "java.util.logging");
        registerJavaPackageModule(Module.create("jdk.management"), "com.sun.management");
        registerJavaPackageModule(Module.create("jdk.jsobject"), "netscape.javascript");

        // JavaFx
        registerJavaPackageModule(Module.create("javafx.base"), "javafx.beans", "javafx.beans.binding", "javafx.beans.property", "javafx.beans.value", "javafx.collections", "javafx.collections.transformation", "javafx.event", "javafx.util");
        registerJavaPackageModule(Module.create("javafx.graphics"), "javafx.animation", "javafx.application", "javafx.css", "javafx.concurrent", "javafx.geometry", "javafx.scene", "javafx.scene.effect", "javafx.scene.image", "javafx.scene.input", "javafx.scene.layout", "javafx.scene.paint", "javafx.scene.shape", "javafx.scene.text", "javafx.scene.transform", "javafx.stage");
        registerJavaPackageModule(Module.create("javafx.controls"), "javafx.scene.control", "javafx.scene.control.skin", "javafx.scene.chart");
        registerJavaPackageModule(Module.create("javafx.web"), "javafx.scene.web");
        registerJavaPackageModule(Module.create("javafx.swing"), "javafx.embed.swing");

        // JavaFx SVG
        registerJavaPackageModule(Module.create("javafxsvg"), "de.codecentric.centerdevice.javafxsvg");

        // GWT
        registerJavaPackageModule(Module.create("gwt-user"), "com.google.gwt.user.client", "com.google.gwt.core.client", "com.google.gwt.resources.client", "com.google.gwt.regexp.shared", "com.google.gwt.storage.client");
        registerJavaPackageModule(Module.create("jsinterop-annotations"), "jsinterop.annotations");
        registerJavaPackageModule(Module.create("elemental2-dom"), "elemental2.dom");

        // GWT charts
        registerJavaPackageModule(Module.create("gwt-charts"), "com.googlecode.gwt.charts.client", "com.googlecode.gwt.charts.client.corechart");

        // Vert.x
        registerJavaPackageModule(Module.create("vertx-core"), "io.vertx.core", "io.vertx.core.eventbus", "io.vertx.core.http", "io.vertx.core.json", "io.vertx.core.net");
        registerJavaPackageModule(Module.create("vertx-web"), "io.vertx.ext.web", "io.vertx.ext.web.handler", "io.vertx.ext.web.handler.sockjs");
        registerJavaPackageModule(Module.create("vertx-bridge-common"), "io.vertx.ext.bridge");
        registerJavaPackageModule(Module.create("vertx-sql-common"), "io.vertx.ext.sql");
        registerJavaPackageModule(Module.create("vertx-jdbc-client"), "io.vertx.ext.jdbc");
        registerJavaPackageModule(Module.create("vertx-mysql-postgresql-client"), "io.vertx.ext.asyncsql");

        // JavaWebSocket
        registerJavaPackageModule(Module.create("Java.WebSocket"), "org.java_websocket.client", "org.java_websocket.drafts", "org.java_websocket.enums", "org.java_websocket.handshake");

        // HikariCP
        registerJavaPackageModule(Module.create("HikariCP"), "com.zaxxer.hikari");
    }

    void registerJavaPackageModule(Module module, String... javaPackages) {
        for (String javaPackage : javaPackages)
            registerJavaPackageModule(javaPackage, module);
    }

    void registerJavaPackageModule(String javaPackage, Module module) {
        Module m = javaPackagesModules.get(javaPackage);
        if (m != null && m != module)
            warning(module + " and " + m + " share the same package " + javaPackage);
        else
            javaPackagesModules.put(javaPackage, module);
    }

    void registerJavaPackagesProjectModule(ProjectModule module) {
        module.getDeclaredJavaClasses().forEach(javaClass -> registerJavaPackageModule(javaClass.getPackageName(), module));
    }

    Module getJavaPackageModule(String javaPackage) {
        Module module = javaPackagesModules.get(javaPackage);
        if (module == null)
            thisAndChildrenModulesInDepthResume.takeWhile(m -> javaPackagesModules.get(javaPackage) == null).forEach(this::registerJavaPackagesProjectModule);
        module = javaPackagesModules.get(javaPackage);
        if (module == null)
            throw new IllegalArgumentException("Unknown module for package " + javaPackage);
        return module;
    }


    /*****************************
     ***** Analyzing streams *****
     *****************************/

    static Collection<Collection<Module>> analyzeDependenciesPathsBetween(Module sourceModule, Module destinationModule) {
        return analyzeDependenciesPathsBetween(new ArrayList<>(), sourceModule, destinationModule);
    }

    private static Collection<Collection<Module>> analyzeDependenciesPathsBetween(Collection<Module> parentPath, Module sourceModule, Module destinationModule) {
        Collection<Collection<Module>> paths = new ArrayList<>();
        if (!parentPath.contains(sourceModule)) { // Skipping cyclic dependencies
            Collection<Module> extendedPath = extendModuleCollection(parentPath, sourceModule);
            if (destinationModule == sourceModule)
                paths.add(extendedPath);
            else if (sourceModule instanceof ProjectModule)
                ((ProjectModule) sourceModule).getDirectDependencies()
                        .map(depModule -> analyzeDependenciesPathsBetween(extendedPath, depModule, destinationModule))
                        .forEach(paths::addAll);
        }
        return paths;
    }

    ReusableStream<Collection<Module>> analyzeCyclicDependenciesLoops() {
        return cyclicDependencyLoopsCache;
    }

    static List<Collection<Module>> analyzeCyclicDependenciesLoops(Module module) {
        return analyzeCyclicDependenciesLoops(new ArrayList<>(), module);
    }

    private static List<Collection<Module>> analyzeCyclicDependenciesLoops(List<Module> parentPath, Module module) {
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
            ((ProjectModule) module).getDirectDependencies()
                    .map(depModule -> analyzeCyclicDependenciesLoops(extendedPath, depModule))
                    .forEach(paths::addAll);
        }
        return paths;
    }

    Collection<Module> getModulesInCyclicDependenciesLoop(Module m1, Module m2) {
        return analyzeCyclicDependenciesLoops()
                .filter(loop -> loop.contains(m1) && loop.contains(m2))
                .findFirst()
                .orElse(null);
    }

    ProjectModule findModuleDeclaringJavaService(String javaService) {
        return getThisAndChildrenModulesInDepth()
                .filter(m -> m.declaresJavaService(javaService))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unable to find " + javaService + " service declaration module"))
                ;
    }

    ReusableStream<ProjectModule> findModulesProvidingJavaService(String javaService) {
        return getThisAndChildrenModulesInDepth()
                .filter(m -> m.providesJavaService(javaService))
                ;
    }

    ProjectModule findBestMatchModuleProvidingJavaService(String javaService, TargetTag... tags) {
        return findBestMatchModuleProvidingJavaService(javaService, new Target(tags));
    }

    ProjectModule findBestMatchModuleProvidingJavaService(String javaService, Target requestedTarget) {
        return getThisAndChildrenModulesInDepth()
                .filter(m -> m.getTarget().gradeTargetMatch(requestedTarget) >= 0)
                .filter(m -> m.providesJavaService(javaService))
                .max(Comparator.comparingInt(m -> m.getTarget().gradeTargetMatch(requestedTarget)))
                .orElseThrow(() -> new IllegalArgumentException("Unable to find " + javaService + " service implementation for requested target " + requestedTarget))
                ;
    }

    /**********************************
     ***** Static utility methods *****
     **********************************/

    private static List<Module> extendModuleCollection(Collection<Module> parentPath, Module module) {
        List<Module> newCollection = new ArrayList<>(parentPath);
        newCollection.add(module);
        return newCollection;
    }

    private static void warning(Object message) {
        System.err.println("WARNING: " + message);
    }
}
