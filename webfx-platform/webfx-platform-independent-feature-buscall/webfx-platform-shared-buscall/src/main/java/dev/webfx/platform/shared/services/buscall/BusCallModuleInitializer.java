package dev.webfx.platform.shared.services.buscall;

import dev.webfx.platform.shared.services.buscall.spi.BusCallEndpoint;
import dev.webfx.platform.shared.services.log.Logger;
import dev.webfx.platform.shared.services.appcontainer.spi.ApplicationModuleInitializer;
import dev.webfx.platform.shared.services.bus.BusService;
import dev.webfx.platform.shared.util.collection.Collections;

import java.util.List;
import java.util.ServiceLoader;

/**
 * @author Bruno Salmon
 */
public final class BusCallModuleInitializer implements ApplicationModuleInitializer {

    @Override
    public String getModuleName() {
        return "webfx-platform-shared-buscall";
    }

    @Override
    public int getInitLevel() {
        return JOBS_START_INIT_LEVEL;
    }

    @Override
    public void initModule() {
        StringBuilder sb = new StringBuilder();
        List<BusCallEndpoint> endpoints = Collections.listOf(ServiceLoader.load(BusCallEndpoint.class));
        for (BusCallEndpoint endpoint : endpoints) {
            BusCallService.registerBusCallEndpoint(endpoint);
            sb.append(sb.length() == 0 ? endpoints.size() + " endpoints provided for addresses: " : ", ").append(endpoint.getAddress());
        }
        Logger.log(sb);
        // Initializing the bus immediately to make the connection connection process happen while the application is initializing
        BusService.bus(); // Instantiating the bus (if not already done) is enough to open the connection
    }
}
