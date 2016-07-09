package naga.providers.platform.client.j2objc;

import naga.platform.client.bus.ReconnectBus;
import naga.platform.client.websocket.WebSocketFactory;
import naga.platform.bus.BusFactory;
import naga.platform.spi.client.ClientPlatform;
import naga.platform.spi.Platform;
import naga.platform.services.resource.spi.ResourceService;

/**
 * @author Bruno Salmon
 */
public class J2ObjCPlatform extends Platform implements ClientPlatform {

    public static void register() {
        Platform.register(new J2ObjCPlatform());
    }

    public J2ObjCPlatform() {
        super(null);
        throw new UnsupportedOperationException("J2ObjCPlatform is not yet implemented");
    }

    @Override
    public WebSocketFactory webSocketFactory() {
        return null;
    }

    @Override
    public BusFactory busFactory() { // busFactory() ClientPlatform default method doesn't work to implement Platform one
        return ReconnectBus::new; // So repeating it again...
    }

    @Override
    public ResourceService resourceService() {
        return null;
    }
}
