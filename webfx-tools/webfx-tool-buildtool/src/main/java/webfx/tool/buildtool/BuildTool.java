package webfx.tool.buildtool;

import webfx.tool.buildtool.util.reusablestream.ReusableStream;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author Bruno Salmon
 */
public final class BuildTool {

    public static void main(String[] args) {
        long t0 = System.currentTimeMillis();
        RootModule webfxRootModule = new RootModule(getWebfxRootDirectory());
        //webfxRootModule.getThisAndChildrenModulesInDepth().forEach(m -> System.out.println(m.getArtifactId() + " : " + m.compatiblePlatforms().collect(Collectors.toList())));
        //webfxRootModule.getThisAndChildrenModulesInDepth().forEach(m -> System.out.println(m.getArtifactId() + " : " + m.getUsedJavaServices().collect(Collectors.toList())));
        ReusableStream<Module> transitiveDependencies = webfxRootModule.getChildModuleInDepth("mongoose-backend-application")
                .getThisAndTransitiveDependencies();
        //transitiveDependencies.forEach(System.out::println);
        ReusableStream<ProjectModule> transitiveProjectModules = transitiveDependencies
                .filter(m -> m instanceof ProjectModule)
                .map(m -> (ProjectModule) m);
        transitiveProjectModules
                .flatMap(ProjectModule::getUsedRequiredJavaServices)
                .distinct()
                .filter(s -> transitiveProjectModules.noneMatch(m -> m.providesJavaService(s)))
                .forEach(s -> System.out.println(s + " -> " + webfxRootModule.findBestMatchModuleProvidingJavaService(s, TargetTag.GWT)))
        ;

        webfxRootModule.getChildModuleInDepth("mongoose-backend-application")
                .findModulesProvidingRequiredService(TargetTag.GWT)
                .forEach(System.out::println);

        ModuleReporter reporter = new ModuleReporter(webfxRootModule);
        reporter.listChildrenModulesInDepth();
        reporter.listThisAndChildrenModulesInDepthWithTheirDirectDependencies();
        reporter.listOrAndChildrenModulesInDepthDirectlyDependingOn("gwt-user");
        reporter.listInDepthTransitiveDependencies("mongoose-backend-application");
        reporter.listDependenciesPathsBetween("mongoose-backend-application", "webfx-fxkit-gwt");
        reporter.listProjectModuleDirectDependencies("webfx-fxkit-emul-javafxgraphics");
        reporter.listProjectModuleJavaClasses("webfx-fxkit-emul-javafxbase");
        reporter.listProjectModuleJavaClassesDependingOn("webfx-fxkit-extra", "webfx-fxkit-gwt");
        reporter.listCyclicDependenciesPaths();
        reporter.listProjectModuleJavaClassesDependingOn("webfx-framework-shared-entity", "webfx-framework-client-uifilter");

        long t1 = System.currentTimeMillis();
        System.out.println("Executed in " + (t1 - t0) + "ms");
    }

    private static Path getWebfxRootDirectory() {
        // Getting targetClassPath (ex: /C:/dev/git-repos/webfx/webfx-tools/webfx-tool-buildhelper/target/classes/)
        String targetClassPath = BuildTool.class.getProtectionDomain().getCodeSource().getLocation().getPath();
        String webfxRootToken = "/webfx/";
        int webfxRootTokenPos = targetClassPath.indexOf(webfxRootToken);
        return Paths.get(targetClassPath.substring(1, webfxRootTokenPos + webfxRootToken.length()));
    }
}
