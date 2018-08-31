package webfx.providers.platform.abstr.java.services.shutdown;

import webfx.platform.services.shutdown.spi.ShutdownProvider;

/**
 * @author Bruno Salmon
 */
public class JavaShutdownProviderImpl implements ShutdownProvider {

    @Override
    public void addShutdownHook(Runnable hook) {
        Runtime.getRuntime().addShutdownHook(new Thread(hook));
    }
}
