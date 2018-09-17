package webfx.platforms.core.services.update;

import webfx.platforms.core.services.appcontainer.spi.ApplicationModuleInitializer;

/**
 * @author Bruno Salmon
 */
public class UpdateModuleInitializer implements ApplicationModuleInitializer {

    @Override
    public String getModuleName() {
        return "webfx-platforms-core-update";
    }

    @Override
    public int getInitLevel() {
        return JSON_CODEC_INIT_LEVEL;
    }

    @Override
    public void initModule() {
        // Registering all required json codecs (especially for network bus calls)
        UpdateService.registerJsonCodecsAndBusCalls();
    }
}
