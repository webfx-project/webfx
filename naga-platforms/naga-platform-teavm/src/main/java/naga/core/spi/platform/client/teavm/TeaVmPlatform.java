package naga.core.spi.platform.client.teavm;

import naga.core.composite.CompositesFactory;
import naga.core.spi.json.teavm.TeaVmJsonObject;
import naga.core.spi.platform.Platform;
import naga.core.spi.platform.Scheduler;
import naga.core.spi.platform.client.ResourceService;
import naga.core.spi.platform.client.WebSocketFactory;
import naga.core.spi.platform.client.web.WebLocation;
import naga.core.spi.platform.client.web.WebPlatform;

/**
 * @author Bruno Salmon
 */
public final class TeaVmPlatform extends WebPlatform {

    public static void register() {
        Platform.register(new TeaVmPlatform());
    }

    private final Scheduler scheduler = new TeaVmScheduler();
    private final CompositesFactory jsonFactory = new TeaVmJsonObject();
    private final WebSocketFactory webSocketFactory = new TeaVmWebSocketFactory();

    @Override
    public Scheduler scheduler() {
        return scheduler;
    }

    @Override
    public CompositesFactory jsonFactory() {
        return jsonFactory;
    }

    @Override
    public WebSocketFactory webSocketFactory() {
        return webSocketFactory;
    }

    @Override
    public ResourceService resourceService() {
        return TeaVmResourceService.SINGLETON;
    }

    @Override
    public WebLocation getCurrentLocation() {
        return TeaVmLocation.current();
    }
}
