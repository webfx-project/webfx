package webfx.tool.buildtool;

import webfx.tool.buildtool.util.reusablestream.ReusableStream;

import java.nio.file.Path;
import java.util.*;

/**
 * @author Bruno Salmon
 */
public final class RootModule extends ProjectModule {

    private final Map<String, Module> thirdPartyModules = new HashMap<>();
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
        registerJavaPackageModule(createThirdPartyModule("java-base"), "java.io", "java.lang", "java.lang.annotation", "java.lang.management", "java.lang.ref", "java.lang.reflect", "java.math", "java.net", "java.nio", "java.nio.charset", "java.nio.file", "java.nio.file.attribute", "java.security", "java.text", "java.time", "java.time.format", "java.time.temporal", "java.util", "java.util.function", "java.util.regex", "java.util.stream", "java.util.concurrent", "java.util.concurrent.atomic");
        registerJavaPackageModule(createThirdPartyModule("java-xml"), "javax.xml", "javax.xml.parsers", "javax.xml.transform", "javax.xml.transform.dom", "javax.xml.transform.stream", "javax.xml.namespace", "javax.xml.xpath", "org.w3c.dom", "org.xml.sax");
        registerJavaPackageModule(createThirdPartyModule("java-sql"), "java.sql", "javax.sql");
        registerJavaPackageModule(createThirdPartyModule("java-logging"), "java.util.logging");
        registerJavaPackageModule(createThirdPartyModule("jdk-management"), "com.sun.management");
        registerJavaPackageModule(createThirdPartyModule("jdk-jsobject"), "netscape.javascript");

        // JavaFx
        //registerJavaPackageModule(createThirdPartyModule("javafx-base"), "javafx.beans", "javafx.beans.binding", "javafx.beans.property", "javafx.beans.value", "javafx.collections", "javafx.collections.transformation", "javafx.event", "javafx.util");
        //registerJavaPackageModule(createThirdPartyModule("javafx-graphics"), "javafx.animation", "javafx.application", "javafx.css", "javafx.concurrent", "javafx.geometry", "javafx.scene", "javafx.scene.effect", "javafx.scene.image", "javafx.scene.input", "javafx.scene.layout", "javafx.scene.paint", "javafx.scene.shape", "javafx.scene.text", "javafx.scene.transform", "javafx.stage");
        //registerJavaPackageModule(createThirdPartyModule("javafx-controls"), "javafx.scene.control", "javafx.scene.control.skin", "javafx.scene.chart");
        registerJavaPackageModule(createThirdPartyModule("javafx-graphics"), "javafx.concurrent");
        registerJavaPackageModule(createThirdPartyModule("javafx-controls"), "javafx.scene.chart");
        registerJavaPackageModule(createThirdPartyModule("javafx-web"), "javafx.scene.web");
        registerJavaPackageModule(createThirdPartyModule("javafx-swing"), "javafx.embed.swing");

        // JavaFx SVG
        registerJavaPackageModule(createThirdPartyModule("javafxsvg"), "de.codecentric.centerdevice.javafxsvg");

        // GWT
        registerJavaPackageModule(createThirdPartyModule("gwt-user"), "com.google.gwt.user.client", "com.google.gwt.core.client", "com.google.gwt.resources.client", "com.google.gwt.regexp.shared", "com.google.gwt.storage.client");
        registerJavaPackageModule(createThirdPartyModule("jsinterop-annotations"), "jsinterop.annotations");
        registerJavaPackageModule(createThirdPartyModule("elemental2-dom"), "elemental2.dom");
        registerJavaPackageModule(createThirdPartyModule("elemental2-svg"), "elemental2.svg");

        // GWT charts
        registerJavaPackageModule(createThirdPartyModule("gwt-charts"), "com.googlecode.gwt.charts.client", "com.googlecode.gwt.charts.client.corechart");

        // Vert.x
        registerJavaPackageModule(createThirdPartyModule("vertx-core"), "io.vertx.core", "io.vertx.core.eventbus", "io.vertx.core.http", "io.vertx.core.json", "io.vertx.core.net");
        registerJavaPackageModule(createThirdPartyModule("vertx-web"), "io.vertx.ext.web", "io.vertx.ext.web.handler", "io.vertx.ext.web.handler.sockjs");
        registerJavaPackageModule(createThirdPartyModule("vertx-bridge-common"), "io.vertx.ext.bridge");
        registerJavaPackageModule(createThirdPartyModule("vertx-sql-common"), "io.vertx.ext.sql");
        registerJavaPackageModule(createThirdPartyModule("vertx-jdbc-client"), "io.vertx.ext.jdbc");
        registerJavaPackageModule(createThirdPartyModule("vertx-mysql-postgresql-client-jasync"), "io.vertx.ext.asyncsql");

        // JavaWebSocket
        registerJavaPackageModule(createThirdPartyModule("Java-WebSocket"), "org.java_websocket", "org.java_websocket.client", "org.java_websocket.drafts", "org.java_websocket.enums", "org.java_websocket.handshake");

        // HikariCP
        registerJavaPackageModule(createThirdPartyModule("HikariCP"), "com.zaxxer.hikari");
    }

    void registerJavaPackageModule(Module module, String... javaPackages) {
        for (String javaPackage : javaPackages)
            registerJavaPackageModule(javaPackage, module);
    }

    void registerJavaPackageModule(String javaPackage, Module module) {
        Module m = javaPackagesModules.get(javaPackage);
        if (m != null && m != module) {
            warning(module + " and " + m + " share the same package " + javaPackage);
            // Should always return, the exception is a hack to replace m = webfx-fxkit-gwt with module = webfx-fxkit-mapper-extracontrols (they share the same package webfx.fxkit.extra.cell.collator.grid)
            if (!(m instanceof ProjectModule) || ((ProjectModule) m).getTarget().isPlatformSupported(Platform.JRE))
                return;
        }
        javaPackagesModules.put(javaPackage, module);
    }

    void registerJavaPackagesProjectModule(ProjectModule module) {
        module.getDeclaredJavaPackages().forEach(javaPackage -> registerJavaPackageModule(javaPackage, module));
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


    public Module findModule(String name) {
        Module module = thirdPartyModules.get(name);
        if (module == null) {
            module = javaPackagesModules.values().stream().filter(m -> m.getName().equals(name)).findFirst().orElseGet(() -> findProjectModule(name, true));
            if (module == null)
                module = getOrCreateThirdPartyModule(name);
        }
        return module;
    }

    Module getOrCreateThirdPartyModule(String artifactId) {
        Module module = getThirdPartyModule(artifactId);
        if (module == null)
            module = createThirdPartyModule(artifactId);
        return module;
    }

    Module getThirdPartyModule(String artifactId) {
        return thirdPartyModules.get(artifactId);
    }

    private Module createThirdPartyModule(String artifactId) {
        Module module = Module.create(artifactId);
        thirdPartyModules.put(artifactId, module);
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
                ((ProjectModule) sourceModule).getDirectModules()
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
            ((ProjectModule) module).getDirectModules()
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
        return findBestMatchModuleProvidingJavaService(getThisAndChildrenModulesInDepth(), javaService, requestedTarget);
    }

    static ProjectModule findBestMatchModuleProvidingJavaService(ReusableStream<ProjectModule> implementationScope, String javaService, TargetTag... tags) {
        return findBestMatchModuleProvidingJavaService(implementationScope, javaService, new Target(tags));
    }

    static ProjectModule findBestMatchModuleProvidingJavaService(ReusableStream<ProjectModule> implementationScope, String javaService, Target requestedTarget) {
        return findModulesProvidingJavaService(implementationScope, javaService, requestedTarget, true).iterator().next();
    }

    public static ReusableStream<ProjectModule> findModulesProvidingJavaService(ReusableStream<ProjectModule> implementationScope, String javaService, Target requestedTarget, boolean keepBestOnly) {
        ReusableStream<ProjectModule> modules = implementationScope
                .filter(m -> m.isCompatibleWithTarget(requestedTarget))
                .filter(m -> m.providesJavaService(javaService));
        if (keepBestOnly)
            modules = ReusableStream.of(modules
                    .max(Comparator.comparingInt(m -> m.gradeTargetMatch(requestedTarget)))
                    .orElse(null)
                    //.orElseThrow(() -> new IllegalArgumentException("Unable to find " + javaService + " service implementation for requested target " + requestedTarget + " within " + implementationScope.collect(Collectors.toList())))
            ).filter(Objects::nonNull);
        return modules;
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
