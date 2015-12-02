package naga.core.spi.platform.teavm;

import naga.core.spi.json.JsonFactory;
import naga.core.spi.json.teavm.TeaVmJsonFactory;
import naga.core.spi.platform.Platform;
import naga.core.spi.platform.client.ClientPlatform;
import naga.core.spi.platform.Scheduler;
import naga.core.spi.sched.teavm.TeaVmScheduler;
import naga.core.spi.platform.client.WebSocketFactory;
import naga.core.spi.sock.teavm.TeaVmWebSocketFactory;

import java.util.logging.Logger;

/**
 * @author Bruno Salmon
 */
public final class TeaVmPlatform extends ClientPlatform {

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

}
