package webfx.platforms.core.services.appcontainer.spi;

/**
 * @author Bruno Salmon
 */
public interface ApplicationModuleInitializer {

    int INIT_LEVEL_1 = 1;
    int INIT_LEVEL_2 = 2;
    int INIT_LEVEL_3 = 3;
    int INIT_LEVEL_4 = 4;
    int INIT_LEVEL_5 = 5;

    int RESOURCE_BUNDLE_INIT_LEVEL = INIT_LEVEL_1;
    int JSON_CODEC_INIT_LEVEL = INIT_LEVEL_2;
    int BUS_START_INIT_LEVEL = INIT_LEVEL_3;
    int APPLICATION_INIT_LEVEL = INIT_LEVEL_4;
    int APPLICATION_LAUNCH_LEVEL = INIT_LEVEL_5;

    String getModuleName();

    int getInitLevel();

    void initModule();

    default void exitModule() {}

}
