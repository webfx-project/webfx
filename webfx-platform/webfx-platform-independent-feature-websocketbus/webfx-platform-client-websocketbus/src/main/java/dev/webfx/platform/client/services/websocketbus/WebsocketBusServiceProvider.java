package dev.webfx.platform.client.services.websocketbus;

import dev.webfx.platform.shared.services.bus.BusFactory;
import dev.webfx.platform.shared.services.bus.BusOptions;
import dev.webfx.platform.shared.services.bus.spi.impl.BusServiceProviderBase;
import dev.webfx.platform.shared.services.resource.ResourceService;
import dev.webfx.platform.shared.services.json.Json;

/**
 * @author Bruno Salmon
 */
public class WebsocketBusServiceProvider extends BusServiceProviderBase {

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
        String json = ResourceService.getText("webfx/platform/client/services/websocketbus/conf/BusOptions.json").result();
        if (json != null)
            options.applyJson(Json.parseObject(json));
    }

}
