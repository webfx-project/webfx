package webfx.tool.buildhelper;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author Bruno Salmon
 */
public final class BuildHelper {

    public static void main(String[] args) {
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
    }

    private static Path getWebfxRootPath() {
        // Getting targetClassPath (ex: /C:/dev/git-repos/webfx/webfx-tools/webfx-tool-buildhelper/target/classes/)
        String targetClassPath = BuildHelper.class.getProtectionDomain().getCodeSource().getLocation().getPath();
        String webfxRootToken = "/webfx/";
        int webfxRootTokenPos = targetClassPath.indexOf(webfxRootToken);
        return Paths.get(targetClassPath.substring(1, webfxRootTokenPos + webfxRootToken.length()));
    }
}
