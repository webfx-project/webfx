package webfx.tool.buildtool;

import webfx.tool.buildtool.sourcegenerators.GwtFilesGenerator;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author Bruno Salmon
 */
public final class BuildTool {

    public static void main(String[] args) {
        long t0 = System.currentTimeMillis();
        RootModule webfxRootModule = new RootModule(getWebfxRootDirectory());

/*
        ModuleReporter reporter = new ModuleReporter(webfxRootModule);
        reporter.listDependenciesPathsBetween("webfx-kit-javafxgraphics-peers-gwt", "webfx-kit-extracontrols");
        reporter.listProjectModuleJavaClassesDependingOn("webfx-kit-javafxgraphics-peers-gwt", "webfx-kit-extracontrols");
*/


        ProjectModule parentModule = webfxRootModule
                //.findProjectModule("webfx-tutorials")
                ;
        parentModule
                .getThisAndChildrenModulesInDepth()
                .filter(ProjectModule::hasSourceDirectory)
                .forEach(m -> m.getMavenModuleFile().updateAndWrite());
        parentModule
                .getThisAndChildrenModulesInDepth()
                .filter(ProjectModule::hasSourceDirectory)
                .filter(ProjectModule::hasJavaSourceDirectory)
                .filter(m -> m.getTarget().isPlatformSupported(Platform.JRE))
                //.filter(m -> !m.getName().contains("-peers"))
                //.filter(m -> !m.isDirectlyDependingOn("jsinterop-annotations"))
                .forEach(m -> m.getJavaModuleFile().writeFile())
        ;
        parentModule
                .getThisAndChildrenModulesInDepth()
                .filter(m -> m.isExecutable(Platform.GWT))
                .forEach(GwtFilesGenerator::generateGwtFiles);
/*
        webfxRootModule.getChildModuleInDepth("webfx-platform-shared-appcontainer-vertx")
            .getUsedJavaPackages()
                .forEach(System.out::println);
*/
        //GwtServiceLoaderSuperSourceGenerator.generateServiceLoaderSuperSource(webfxRootModule.getChildModuleInDepth("webfx-tutorial-colorfulcircles-application-gwt"));
        //webfxRootModule.getThisAndChildrenModulesInDepth().forEach(m -> System.out.println(m.getArtifactId() + " : " + m.compatiblePlatforms().collect(Collectors.toList())));
        //webfxRootModule.getThisAndChildrenModulesInDepth().forEach(m -> System.out.println(m.getArtifactId() + " : " + m.getUsedJavaServices().collect(Collectors.toList())));
        //webfxRootModule.getChildModuleInDepth("webfx-tutorial-helloworld-fxkit-gwt").getTransitiveRequiredJavaServicesImplementationModules().forEach(System.out::println);

/*
        ReusableStream<Module> transitiveDependencies =
                webfxRootModule.getChildModuleInDepth("webfx-tutorial-helloworld-fxkit-gwt").getThisAndTransitiveDependencies()
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
        reporter.listProjectModuleJavaClassesDependingOn("webfx-framework-shared-entity", "webfx-framework-client-uifilter");
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
