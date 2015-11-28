package naga.core.spi.plat.j2objc;

import naga.core.spi.json.JsonFactory;
import naga.core.spi.sock.WebSocketFactory;
import naga.core.spi.plat.Platform;
import naga.core.spi.sched.Scheduler;

/**
 * @author Bruno Salmon
 */
public class J2ObjCPlatform implements Platform {

    public static void register() {
        Platform.register(new J2ObjCPlatform());
    }

    @Override
    public WebSocketFactory webSocketFactory() {
        throw new UnsupportedOperationException("J2ObjCPlatform.webSocketFactory() is not yet implemented");
    }

    @Override
    public Scheduler scheduler() {
        throw new UnsupportedOperationException("J2ObjCPlatform.scheduler() is not yet implemented");
    }

    @Override
    public JsonFactory jsonFactory() {
        throw new UnsupportedOperationException("J2ObjCPlatform.jsonFactory() is not yet implemented");
    }
}
