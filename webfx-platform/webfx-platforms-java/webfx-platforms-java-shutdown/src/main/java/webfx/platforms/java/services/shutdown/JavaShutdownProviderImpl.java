package webfx.platforms.java.services.shutdown;

import webfx.platforms.core.services.shutdown.spi.ShutdownProvider;

/**
 * @author Bruno Salmon
 */
public final class JavaShutdownProviderImpl implements ShutdownProvider {

    @Override
    public void addShutdownHook(Runnable hook) {
        Runtime.getRuntime().addShutdownHook(new Thread(hook));
    }
}
