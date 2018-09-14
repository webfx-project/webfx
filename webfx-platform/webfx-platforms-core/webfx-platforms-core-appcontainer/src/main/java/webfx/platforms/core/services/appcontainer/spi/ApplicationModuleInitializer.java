package webfx.platforms.core.services.appcontainer.spi;

/**
 * @author Bruno Salmon
 */
public interface ApplicationModuleInitializer {

    String getModuleName();

    void initModule();

    default void exitModule() {}

}
