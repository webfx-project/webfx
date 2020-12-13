package dev.webfx.platform.shared.services.appcontainer.spi.impl;

import dev.webfx.platform.shared.services.log.Logger;
import dev.webfx.platform.shared.services.appcontainer.spi.ApplicationModuleInitializer;
import dev.webfx.platform.shared.util.collection.Collections;

import java.util.Comparator;
import java.util.List;
import java.util.ServiceLoader;

/**
 * @author Bruno Salmon
 */
public final class ApplicationModuleInitializerManager {

    private static final List<ApplicationModuleInitializer> modules = Collections.listOf(ServiceLoader.load(ApplicationModuleInitializer.class));

    public static void initialize() {
        sortModulesByInitLevel();
        startModules();
    }

    public static void shutdown() {
        stopModules();
    }

    private static void sortModulesByInitLevel() {
        modules.sort(Comparator.comparingInt(ApplicationModuleInitializer::getInitLevel));
    }

    private static void startModules() {
        callModules(true);
    }

    private static void stopModules() {
        callModules(false);
    }

    private static void callModules(boolean init) {
        // Calling all registered application modules
        int n = modules.size();
        logInFrame((init ? "Initializing " : "Exiting ") + n + " application modules");
        for (int i = 0; i < n; i++) {
            int moduleIndex = init ? i : n - i - 1;
            ApplicationModuleInitializer module = modules.get(moduleIndex);
            Logger.log((moduleIndex + 1) + ") " + module.getModuleName() + " (level " + module.getInitLevel() + ")");
        }
        for (int i = 0; i < n; i++) {
            int moduleIndex = init ? i : n - i - 1;
            ApplicationModuleInitializer module = modules.get(moduleIndex);
            Logger.log(">>>>> " + (init ? "Initializing " : "Exiting ") + (moduleIndex + 1) + ") " + module.getModuleName() + " with " + module.getClass().getSimpleName() + " <<<<<");
            if (init)
                module.initModule();
            else
                module.exitModule();
        }
        logInFrame(n + " application modules " + (init ? "initialized" : "exited"));
    }

    private static void logInFrame(String s) {
        s = "***** " + s + " *****";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < s.length(); i++)
            sb.append('*');
        Logger.log(sb + "\n" + s + "\n" + sb);
    }
}
