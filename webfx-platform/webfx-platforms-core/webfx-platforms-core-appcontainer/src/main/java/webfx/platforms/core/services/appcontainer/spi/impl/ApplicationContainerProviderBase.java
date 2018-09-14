package webfx.platforms.core.services.appcontainer.spi.impl;

import webfx.platforms.core.services.appcontainer.spi.ApplicationContainerProvider;
import webfx.platforms.core.services.appcontainer.spi.ApplicationModuleInitializer;
import webfx.platforms.core.services.log.Logger;
import webfx.platforms.core.services.shutdown.Shutdown;
import webfx.platforms.core.util.collection.Collections;
import webfx.platforms.core.util.serviceloader.SingleServiceLoader;

import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;

/**
 * @author Bruno Salmon
 */
public class ApplicationContainerProviderBase implements ApplicationContainerProvider {

    private final List<ApplicationModuleInitializer> modules = Collections.listOf(ServiceLoader.load(ApplicationModuleInitializer.class));

    public ApplicationContainerProviderBase() {
        // Caching this instance to make the ApplicationContainer work
        SingleServiceLoader.cacheServiceInstance(ApplicationContainerProvider.class, this);
        init();
    }

    protected void init() {
        startModules();
        Shutdown.addShutdownHook(this::stopModules);
    }

    protected void startModules() {
        callModules(true);
    }

    protected void stopModules() {
        callModules(false);
    }

    protected void callModules(boolean init) {
        // Calling all registered application modules
        int n = modules.size();
        logInFrame((init ? "Initializing " : "Exiting ") + n + " application modules");
        List<ApplicationModuleInitializer> orderedModules = init ? new ArrayList<>(modules.size()) : modules;
        if (init) // Calling in inverse order (from platform layers to application layers) on init
            Collections.forEach(ServiceLoader.load(ApplicationModuleInitializer.class), i -> orderedModules.add(0, i));
        for (int i = 0; i < n; i++)
            Logger.log((i + 1) + ") " + orderedModules.get(i).getModuleName());
        for (int i = 0; i < n; i++) {
            ApplicationModuleInitializer module = orderedModules.get(i);
            Logger.log(">>>>> " + (init ? "Initializing " : "Exiting ") + (i + 1) + ") " + module.getModuleName() + " with " + module.getClass().getSimpleName() + " <<<<<");
            if (init)
                module.initModule();
            else
                module.exitModule();
        }
        logInFrame(n + " application modules " + (init ? "initialized" : "exited"));
    }

    protected void logInFrame(String s) {
        s = "***** " + s + " *****";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < s.length(); i++)
            sb.append('*');
        Logger.log(sb + "\n" + s + "\n" + sb);
    }
}
