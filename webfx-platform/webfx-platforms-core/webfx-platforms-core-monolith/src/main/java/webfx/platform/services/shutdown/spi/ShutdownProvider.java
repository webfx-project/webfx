package webfx.platform.services.shutdown.spi;

/**
 * @author Bruno Salmon
 */
public interface ShutdownProvider {

    void addShutdownHook(Runnable hook);

}
