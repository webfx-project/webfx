package webfx.tool.buildtool;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author Bruno Salmon
 */
public final class BuildTool {

    public static void main(String[] args) {
        long t0 = System.currentTimeMillis();
        RootModule webfxRootModule = new RootModule(getWebfxRootPath());
        webfxRootModule.listChildrenModulesInDepth();
        webfxRootModule.listThisAndChildrenModulesInDepthWithTheirDirectDependencies();
        webfxRootModule.listOrAndChildrenModulesInDepthDirectlyDependingOn("gwt-user");
        webfxRootModule.listInDepthTransitiveDependencies("mongoose-backend-application");
        webfxRootModule.listDependenciesPathsBetween("mongoose-backend-application", "webfx-fxkit-gwt");
        webfxRootModule.listProjectModuleDirectDependencies("webfx-fxkit-emul-javafxgraphics");
        webfxRootModule.listProjectModuleJavaClasses("webfx-fxkit-emul-javafxbase");
        webfxRootModule.listProjectModuleJavaClassesDependingOn("webfx-fxkit-extra", "webfx-fxkit-gwt");
        webfxRootModule.listCyclicDependenciesPaths();
        webfxRootModule.listProjectModuleJavaClassesDependingOn("webfx-framework-shared-entity", "webfx-framework-client-uifilter");
        long t1 = System.currentTimeMillis();
        System.out.println("Executed in " + (t1 - t0) + "ms");
    }

    private static Path getWebfxRootPath() {
        // Getting targetClassPath (ex: /C:/dev/git-repos/webfx/webfx-tools/webfx-tool-buildhelper/target/classes/)
        String targetClassPath = BuildTool.class.getProtectionDomain().getCodeSource().getLocation().getPath();
        String webfxRootToken = "/webfx/";
        int webfxRootTokenPos = targetClassPath.indexOf(webfxRootToken);
        return Paths.get(targetClassPath.substring(1, webfxRootTokenPos + webfxRootToken.length()));
    }
}
