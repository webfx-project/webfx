package naga.core.spi.platform.client.teavm;

import naga.core.spi.json.JsonFactory;
import naga.core.spi.json.teavm.TeaVmJsonFactory;
import naga.core.spi.platform.Platform;
import naga.core.spi.platform.Scheduler;
import naga.core.spi.platform.client.ResourceService;
import naga.core.spi.platform.client.WebSocketFactory;
import naga.core.spi.platform.client.web.WebLocation;
import naga.core.spi.platform.client.web.WebPlatform;

import java.util.logging.Logger;

/**
 * @author Bruno Salmon
 */
public final class TeaVmPlatform extends WebPlatform {

    public static void register() {
        Platform.register(new TeaVmPlatform());
    }

    private final Scheduler scheduler = new TeaVmScheduler();
    private final JsonFactory jsonFactory = new TeaVmJsonFactory();
    private final WebSocketFactory webSocketFactory = new TeaVmWebSocketFactory();

    @Override
    public Logger logger() {
        return Logger.getAnonymousLogger();
    }

    @Override
    public Scheduler scheduler() {
        return scheduler;
    }

    @Override
    public JsonFactory jsonFactory() {
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
