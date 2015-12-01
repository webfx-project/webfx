package naga.core.spi.platform.client.j2objc;

import naga.core.spi.json.JsonFactory;
import naga.core.spi.platform.Platform;
import naga.core.spi.platform.client.ClientPlatform;
import naga.core.spi.platform.Scheduler;
import naga.core.spi.platform.client.WebSocketFactory;

/**
 * @author Bruno Salmon
 */
public class J2ObjCPlatform implements ClientPlatform {

    public static void register() {
        Platform.register(new J2ObjCPlatform());
    }

    @Override
    public Scheduler scheduler() {
        throw new UnsupportedOperationException("J2ObjCPlatform.scheduler() is not yet implemented");
    }

    @Override
    public JsonFactory jsonFactory() {
        throw new UnsupportedOperationException("J2ObjCPlatform.jsonFactory() is not yet implemented");
    }

    @Override
    public WebSocketFactory webSocketFactory() {
        throw new UnsupportedOperationException("J2ObjCPlatform.webSocketFactory() is not yet implemented");
    }
}
