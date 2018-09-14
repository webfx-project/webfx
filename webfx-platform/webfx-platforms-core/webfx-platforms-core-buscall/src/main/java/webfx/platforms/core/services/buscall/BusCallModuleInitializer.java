package webfx.platforms.core.services.buscall;

import webfx.platforms.core.services.appcontainer.spi.ApplicationModuleInitializer;

/**
 * @author Bruno Salmon
 */
public class BusCallModuleInitializer implements ApplicationModuleInitializer {

    @Override
    public String getModuleName() {
        return "webfx-platforms-core-buscall";
    }

    @Override
    public void initModule() {
        BusCallService.registerJsonCodecs();
    }
}
