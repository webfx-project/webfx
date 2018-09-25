package webfx.platforms.core.services.appcontainer.spi;

/**
 * @author Bruno Salmon
 */
public interface ApplicationModuleInitializer {

    int RESOURCE_BUNDLE_INIT_LEVEL = 1;
    int SERIAL_CODEC_INIT_LEVEL = 2;
    int BUS_START_INIT_LEVEL = 3;
    int APPLICATION_INIT_LEVEL = 4;
    int APPLICATION_LAUNCH_LEVEL = 5;

    String getModuleName();

    int getInitLevel();

    void initModule();

    default void exitModule() {}

}
