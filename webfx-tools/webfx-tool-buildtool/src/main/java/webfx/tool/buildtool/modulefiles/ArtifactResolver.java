package webfx.tool.buildtool.modulefiles;

import webfx.tool.buildtool.Module;
import webfx.tool.buildtool.ModuleDependency;
import webfx.tool.buildtool.ProjectModule;
import webfx.tool.buildtool.RootModule;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author Bruno Salmon
 */
final class ArtifactResolver {

    static String getArtifactId(Module module, boolean isForGwt, boolean isExecutable, boolean isRegistry) {
        String moduleName = module.getName();
        if (moduleName.startsWith("java-") || moduleName.startsWith("jdk-"))
            return null;
        switch (moduleName) {
            //case "gwt-charts":
            case "jsinterop-annotations":
                return null;
            case "webfx-fxkit-javafxbase-emul":
                return isForGwt || isRegistry ? moduleName : "javafx-base";
            case "webfx-fxkit-javafxgraphics-emul":
                return isForGwt || isRegistry ? moduleName : "javafx-graphics";
            case "webfx-fxkit-javafxcontrols-emul":
                return isForGwt || isRegistry ? moduleName : "javafx-controls";
            case "com-zaxxer-hikari":
                return "HikariCP";
        }
        if (isRegistry && "javafx-graphics".equals(moduleName))
            return "webfx-fxkit-javafxgraphics-emul";
        if (isForGwt && isExecutable) {
            switch (moduleName) {
                case "elemental2-dom":
                case "javafx-base":
                case "javafx-graphics":
                case "javafx-controls":
                    return null;
                case "gwt-user":
                    return "gwt-dev";
            }
        }
        return moduleName;
    }

    static String getGroupId(Module module, boolean isForGwt, boolean isRegistry) {
        String moduleName = module.getName();
        switch (moduleName) {
            case "elemental2-dom":
            case "elemental2-svg":
                return "com.google.elemental2";
            case "gwt-charts": return "com.googlecode.gwt-charts";
            case "Java-WebSocket": return "org.java-websocket";
            case "com-zaxxer-hikari": return "com.zaxxer";
            case "slf4j-api": return "org.slf4j";
            case "javafxsvg" : return "de.codecentric.centerdevice";
        }
        if (moduleName.startsWith("javafx-") || !isForGwt && !isRegistry && RootModule.isJavaFxEmulModule(moduleName))
            return "org.openjfx";
        if (moduleName.startsWith("gwt-"))
            return "com.google.gwt";
        if (moduleName.startsWith("webfx-"))
            return "${webfx.groupId}";
        if (moduleName.startsWith("mongoose-"))
            return "${mongoose.groupId}";
        if (moduleName.startsWith("vertx-"))
            return "io.vertx";
        return "???";
    }

    static String getVersion(Module module, boolean isForGwt, boolean isRegistry) {
        String moduleName = module.getName();
        switch (moduleName) {
            case "elemental2-dom":
            case "elemental2-svg":
            case "Java-WebSocket":
            case "javafxsvg":
                return null; // Managed by root pom
            case "com-zaxxer-hikari": return "3.3.1";
            case "slf4j-api": return "1.7.15";
        }
        if (moduleName.startsWith("javafx-") || !isForGwt && !isRegistry && RootModule.isJavaFxEmulModule(moduleName))
            return "${lib.openjfx.version}";
        if (moduleName.startsWith("gwt-"))
            return null; // Managed by root pom
        if (moduleName.startsWith("webfx-"))
            return "${webfx.version}";
        if (moduleName.startsWith("mongoose-"))
            return "${mongoose.version}";
        if (moduleName.startsWith("vertx-"))
            return null; // Managed by root pom
        return "???";
    }

    static String getScope(Map.Entry<Module, List<ModuleDependency>> moduleGroup, boolean isForGwt, boolean isForJavaFx, boolean isExecutable, boolean isRegistry) {
        String scope = moduleGroup.getValue().stream().map(ModuleDependency::getScope).filter(Objects::nonNull).findAny().orElse(null);
        if (scope != null)
            return scope;
        Module module = moduleGroup.getKey();
        // Setting scope to "provided" for interface modules and optional dependencies
        if (module instanceof ProjectModule && ((ProjectModule) module).isInterface() || moduleGroup.getValue().stream().anyMatch(ModuleDependency::isOptional))
            return "provided";
        if (!isForGwt && !isForJavaFx && !isExecutable && !isRegistry)
            switch (module.getName()) {
                case "webfx-fxkit-javafxbase-emul":
                case "webfx-fxkit-javafxgraphics-emul":
                case "webfx-fxkit-javafxcontrols-emul":
                case "javafx-base":
                case "javafx-graphics":
                case "javafx-controls":
                    return "provided";
                case "slf4j-api":
                    return "runtime";
            }
        return null;
    }

    static String getClassifier(Map.Entry<Module, List<ModuleDependency>> moduleGroup, boolean isForGwt, boolean isExecutable) {
        String classifier = moduleGroup.getValue().stream().map(ModuleDependency::getClassifier).filter(Objects::nonNull).findAny().orElse(null);
        if (classifier != null)
            return classifier;
        if (isForGwt && isExecutable) {
            String moduleName = moduleGroup.getKey().getName();
            if (!moduleName.startsWith("gwt-") && !moduleName.startsWith("elemental2-"))
                return moduleName.contains("-gwt-emul-") ? "shaded-sources" : "sources";
        }
        return null;
    }
}
