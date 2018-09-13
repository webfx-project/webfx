package webfx.platforms.core.services.bus.client;

import webfx.platforms.core.services.bus.BusFactory;
import webfx.platforms.core.services.bus.BusOptions;
import webfx.platforms.core.services.bus.spi.impl.BusServiceProviderBase;
import webfx.platforms.core.services.json.Json;
import webfx.platforms.core.services.resource.ResourceService;

/**
 * @author Bruno Salmon
 */
public class ClientBusServiceProviderImpl extends BusServiceProviderBase {

    @Override
    public BusFactory busFactory() {
        return ReconnectBus::new;
    }

    @Override
    public BusOptions createBusOptions() {
        return new WebSocketBusOptions();
    }

    @Override
    public void setPlatformBusOptions(BusOptions options) {
        super.setPlatformBusOptions(options);
        String json = ResourceService.getText("webfx/platforms/core/services/bus/client/conf/BusOptions.json").result();
        if (json != null)
            options.applyJson(Json.parseObject(json));
    }

}
