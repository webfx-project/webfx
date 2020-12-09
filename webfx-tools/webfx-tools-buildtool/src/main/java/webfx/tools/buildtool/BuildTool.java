package webfx.tools.buildtool;

import webfx.tools.buildtool.sourcegenerators.GluonFilesGenerator;
import webfx.tools.buildtool.sourcegenerators.GwtFilesGenerator;
import webfx.tools.buildtool.sourcegenerators.JavaFilesGenerator;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

/**
 * @author Bruno Salmon
 */
public final class BuildTool {

    public static void main(String[] args) {
        long t0 = System.currentTimeMillis();

        ProjectModule parentModule = new RootModule(getWebfxRootDirectory(), // Webfx root directory
                Arrays.stream(args).map(Path::of).toArray(Path[]::new));     // Additional project modules passed through command line

        // Updating Maven module files for all source modules (<dependencies> section in pom.xml)
        parentModule
                .getThisAndChildrenModulesInDepth()
                .filter(ProjectModule::hasSourceDirectory)
                .forEach(m -> m.getMavenModuleFile().updateAndWrite());

        // Generating files for Java modules (module-info.java and META-INF/services)
        parentModule
                .getThisAndChildrenModulesInDepth()
                .filter(ProjectModule::hasSourceDirectory)
                .filter(ProjectModule::hasJavaSourceDirectory)
                .filter(m -> m.getTarget().isPlatformSupported(Platform.JRE))
                .forEach(JavaFilesGenerator::generateJavaFiles)
        ;

        // Generate files for executable GWT modules (module.gwt.xml, index.html, super sources, service loader, resource bundle)
        parentModule
                .getThisAndChildrenModulesInDepth()
                .filter(m -> m.isExecutable(Platform.GWT))
                .forEach(GwtFilesGenerator::generateGwtFiles);

        // Generate files for executable Gluon modules (graalvm_config/reflection.json)
        parentModule
                .getThisAndChildrenModulesInDepth()
                .filter(m -> m.isExecutable(Platform.JRE))
                .filter(m -> m.getTarget().hasTag(TargetTag.GLUON))
                .forEach(GluonFilesGenerator::generateGraalVmReflectionJson);


/*
        ModuleReporter reporter = new ModuleReporter(webfxRootModule);
        reporter.listDependenciesPathsBetween("webfx-kit-javafxgraphics-peers-gwt", "webfx-kit-extracontrols");
        reporter.listProjectModuleJavaClassesDependingOn("webfx-kit-javafxgraphics-peers-gwt", "webfx-kit-extracontrols");
*/

/*
        webfxRootModule.getChildModuleInDepth("webfx-platform-shared-appcontainer-vertx")
            .getUsedJavaPackages()
                .forEach(System.out::println);
*/
        //GwtServiceLoaderSuperSourceGenerator.generateServiceLoaderSuperSource(webfxRootModule.getChildModuleInDepth("webfx-demo-colorfulcircles-application-gwt"));
        //webfxRootModule.getThisAndChildrenModulesInDepth().forEach(m -> System.out.println(m.getArtifactId() + " : " + m.compatiblePlatforms().collect(Collectors.toList())));
        //webfxRootModule.getThisAndChildrenModulesInDepth().forEach(m -> System.out.println(m.getArtifactId() + " : " + m.getUsedJavaServices().collect(Collectors.toList())));
        //webfxRootModule.getChildModuleInDepth("webfx-demo-helloworld-fxkit-gwt").getTransitiveRequiredJavaServicesImplementationModules().forEach(System.out::println);

/*
        ReusableStream<Module> transitiveDependencies =
                webfxRootModule.getChildModuleInDepth("webfx-demo-helloworld-fxkit-gwt").getThisAndTransitiveDependencies()
                ;
        transitiveDependencies.forEach(System.out::println);
        ReusableStream<ProjectModule> transitiveProjectModules = ProjectModule.filterProjectModules(transitiveDependencies);
        ReusableStream<ProjectModule> implementationScopeProjectModules = transitiveProjectModules
                .concat(webfxRootModule.getChildModuleInDepth("webfx-platform").getThisAndChildrenModulesInDepth())
                ;
        transitiveProjectModules
                .flatMap(ProjectModule::getUsedRequiredJavaServices)
                .distinct()
                //.filter(s -> transitiveProjectModules.noneMatch(m -> m.providesJavaService(s)))
                .forEach(s -> {
                    ProjectModule m = RootModule.findBestMatchModuleProvidingJavaService(implementationScopeProjectModules, s, TargetTag.GWT);
                    System.out.println(s + " -> " + m);
                    //System.out.println(s + " -> " + (m == null ? "null" : m.getProvidedJavaServiceImplementations(s).collect(Collectors.toList())));
                })
        ;
*/

/*
        webfxRootModule.getChildModuleInDepth("mongoose-backend-application")
                .findModulesProvidingRequiredService(TargetTag.GWT)
                .forEach(System.out::println);

        ModuleReporter reporter = new ModuleReporter(webfxRootModule);
        reporter.listChildrenModulesInDepth();
        reporter.listThisAndChildrenModulesInDepthWithTheirDirectDependencies();
        reporter.listOrAndChildrenModulesInDepthDirectlyDependingOn("gwt-user");
        reporter.listInDepthTransitiveDependencies("mongoose-backend-application");
        reporter.listDependenciesPathsBetween("mongoose-backend-application", "webfx-kit-gwt");
        reporter.listProjectModuleDirectDependencies("webfx-kit-base-emul");
        reporter.listProjectModuleJavaClasses("webfx-kit-javafxbase-emul");
        reporter.listProjectModuleJavaClassesDependingOn("webfx-kit-extracontrols", "webfx-kit-gwt");
        reporter.listCyclicDependenciesPaths();
        reporter.listProjectModuleJavaClassesDependingOn("webfx-framework-shared-orm-entity", "webfx-framework-client-orm-reactive-entities");
*/

        long t1 = System.currentTimeMillis();
        System.out.println("Executed in " + (t1 - t0) + "ms");
    }

    private static Path getWebfxRootDirectory() {
        // Getting targetClassPath (ex: /C:/dev/git-repos/webfx/webfx-tools/webfx-tool-buildhelper/target/classes/)
        String targetClassPath = BuildTool.class.getProtectionDomain().getCodeSource().getLocation().getPath();
        String webfxRootToken = "/webfx/";
        int webfxRootTokenPos = targetClassPath.indexOf(webfxRootToken);
        // Note: Paths.get("/C:/dev/...") raises an exception, but Paths.get("C:/dev/...") works
        boolean windowsDrive = targetClassPath.length() >= 3 && targetClassPath.charAt(2) == ':'; // Ex: /C:/dev/...
        return Paths.get(targetClassPath.substring(windowsDrive ? 1 : 0, webfxRootTokenPos + webfxRootToken.length()));
    }
}
