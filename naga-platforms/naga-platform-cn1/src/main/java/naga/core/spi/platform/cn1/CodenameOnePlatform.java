package naga.core.spi.platform.cn1;

import naga.core.bus.client.ReconnectBus;
import naga.core.spi.platform.WebSocketFactory;
import naga.core.spi.platform.BusFactory;
import naga.core.spi.platform.ClientPlatform;
import naga.core.spi.platform.Platform;
import naga.core.services.resource.ResourceService;

/**
 * @author Bruno Salmon
 */
public class CodenameOnePlatform extends Platform implements ClientPlatform {

    /**
     * Providing JrePlatform.register() method if needed but this explicit call is normally not necessary
     * as this platform is listed in META-INFO/services and can therefore be found by the ServiceLoader.
     */
    public static void register() {
        Platform.register(new CodenameOnePlatform());
    }

    private final WebSocketFactory cn1WebSocketFactory = new Cn1WebSocketFactory();

    public CodenameOnePlatform() {
        super(Cn1Scheduler.SINGLETON);
    }

    @Override
    public BusFactory busFactory() { // busFactory() ClientPlatform default method doesn't work to implement Platform one
        return ReconnectBus::new; // So repeating it again...
    }

    @Override
    public WebSocketFactory webSocketFactory() {
        return cn1WebSocketFactory;
    }

    @Override
    public ResourceService resourceService() {
        return Cn1ResourceService.SINGLETON;
    }


}
