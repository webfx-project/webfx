package naga.providers.platform.abstr.java.services.shutdown;

import naga.platform.services.shutdown.spi.ShutdownProvider;

/**
 * @author Bruno Salmon
 */
public class JavaShutdownProvider implements ShutdownProvider {

    @Override
    public void addShutdownHook(Runnable hook) {
        Runtime.getRuntime().addShutdownHook(new Thread(hook));
    }
}
