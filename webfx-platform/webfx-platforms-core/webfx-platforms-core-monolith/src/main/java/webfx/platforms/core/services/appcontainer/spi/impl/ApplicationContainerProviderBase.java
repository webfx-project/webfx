package webfx.platforms.core.services.appcontainer.spi.impl;

import webfx.platforms.core.services.appcontainer.spi.ApplicationContainerProvider;
import webfx.platforms.core.services.appcontainer.spi.ApplicationModule;
import webfx.platforms.core.services.log.Logger;
import webfx.platforms.core.services.shutdown.Shutdown;
import webfx.platforms.core.util.collection.Collections;
import webfx.platforms.core.util.serviceloader.ServiceLoaderHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;

/**
 * @author Bruno Salmon
 */
public class ApplicationContainerProviderBase implements ApplicationContainerProvider {

    private final List<ApplicationModule> modules = Collections.listOf(ServiceLoader.load(ApplicationModule.class));

    public ApplicationContainerProviderBase() {
        // Caching this instance to make the ApplicationContainer work
        ServiceLoaderHelper.cacheServiceInstance(ApplicationContainerProvider.class, this);
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

    protected void callModules(boolean starting) {
        // Calling all registered application modules
        int n = modules.size();
        logInFrame((starting ? "Starting " : "Stopping ") + n + " application modules");
        List<ApplicationModule> orderedModules = starting ? new ArrayList<>(modules.size()) : modules;
        if (starting) // Calling in inverse order (from platform layers to application layers) when starting
            Collections.forEach(ServiceLoader.load(ApplicationModule.class), i -> orderedModules.add(0, i));
        for (int i = 0; i < n; i++)
            Logger.log((i + 1) + ") " + orderedModules.get(i).getClass().getName());
        for (int i = 0; i < n; i++) {
            ApplicationModule module = orderedModules.get(i);
            Logger.log(">>>>> " + (starting ? "Starting " : "Stopping ") + (i + 1) + ") " + module.getClass().getName() + " <<<<<");
            if (starting)
                module.start();
        }
        logInFrame(n + " application modules " + (starting ? "started" : "stopped"));
    }

    protected void logInFrame(String s) {
        s = "***** " + s + " *****";
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < s.length(); i++)
            sb.append('*');
        Logger.log(sb + "\n" + s + "\n" + sb);
    }
}
