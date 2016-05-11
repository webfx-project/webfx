package naga.core.spi.platform.client.cn1;

import naga.core.json.JsonFactory;
import naga.core.spi.platform.Platform;
import naga.core.spi.platform.Scheduler;
import naga.core.spi.platform.client.ClientPlatform;
import naga.core.spi.platform.client.ResourceService;
import naga.core.spi.platform.client.WebSocketFactory;

/**
 * @author Bruno Salmon
 */
public class CodenameOnePlatform extends ClientPlatform {

    /**
     * Providing JrePlatform.register() method if needed but this explicit call is normally not necessary
     * as this platform is listed in META-INFO/services and can therefore be found by the ServiceLoader.
     */
    public static void register() {
        Platform.register(new CodenameOnePlatform());
    }

    private final WebSocketFactory cn1WebSocketFactory = new Cn1WebSocketFactory();

    @Override
    public WebSocketFactory webSocketFactory() {
        return cn1WebSocketFactory;
    }

    @Override
    public ResourceService resourceService() {
        return Cn1ResourceService.SINGLETON;
    }

    @Override
    public Scheduler scheduler() {
        return Cn1Scheduler.SINGLETON;
    }

    @Override
    public JsonFactory jsonFactory() {
        return new Cn1JsonObject();
    }
}
