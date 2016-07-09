package naga.providers.platform.client.cn1;

import naga.platform.bus.BusFactory;
import naga.platform.client.bus.ReconnectBus;
import naga.platform.services.resource.spi.ResourceService;
import naga.commons.util.Numbers;
import naga.commons.util.numbers.providers.CldcPlatformNumbers;
import naga.platform.client.websocket.WebSocketFactory;
import naga.platform.spi.client.ClientPlatform;
import naga.platform.spi.Platform;
import naga.providers.platform.client.cn1.scheduler.Cn1Scheduler;
import naga.providers.platform.client.cn1.services.resource.Cn1ResourceService;
import naga.providers.platform.client.cn1.websocket.Cn1WebSocketFactory;

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
        // As Codename One is based on CLDC platform, we must use the CldcPlatformNumbers provider.
        Numbers.registerNumbersProvider(new CldcPlatformNumbers());
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
