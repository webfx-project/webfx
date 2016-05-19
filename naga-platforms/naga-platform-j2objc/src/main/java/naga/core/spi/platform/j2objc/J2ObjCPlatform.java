package naga.core.spi.platform.j2objc;

import naga.core.client.bus.ReconnectBus;
import naga.core.client.bus.WebSocketFactory;
import naga.core.spi.bus.BusFactory;
import naga.core.spi.platform.ClientPlatform;
import naga.core.spi.platform.Platform;
import naga.core.spi.platform.ResourceService;

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
