package dev.webfx.platform.shared.services.shutdown.spi.impl;

import dev.webfx.platform.shared.services.shutdown.spi.ShutdownProvider;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Bruno Salmon
 */
public abstract class ShutdownProviderBase<H> implements ShutdownProvider {

    private final Map<Runnable, H> platformHooks = new HashMap<>();
    private boolean shuttingDown;
    private boolean softwareShutdown;

    @Override
    public boolean isShuttingDown() {
        return shuttingDown;
    }

    @Override
    public boolean isSoftwareShutdown() {
        return softwareShutdown;
    }

    @Override
    public void addShutdownHook(Runnable hook) {
        H platformHook = createPlatformShutdownHook(() -> {
            shuttingDown = true;
            hook.run();
        });
        platformHooks.put(hook, platformHook);
        addPlatformShutdownHook(platformHook);
    }

    protected abstract H createPlatformShutdownHook(Runnable hook);

    protected abstract void addPlatformShutdownHook(H platformHook);

    @Override
    public void removeShutdownHook(Runnable hook) {
        removePlatformShutdownHook(platformHooks.remove(hook));
    }

    protected abstract void removePlatformShutdownHook(H platformHook);

    @Override
    public void softwareShutdown(boolean exit, int exitStatus) {
        softwareShutdown = shuttingDown = true;
        Collection<Runnable> hooks = new ArrayList<>(platformHooks.keySet());
        for (Runnable hook : hooks)
            removeShutdownHook(hook);
        for (Runnable hook : hooks)
            hook.run();
        if (exit)
            exit(exitStatus);
    }

    protected abstract void exit(int exitStatus);
}
