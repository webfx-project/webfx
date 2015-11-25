package naga.core.spi.plat.teavm;

import naga.core.spi.json.JsonFactory;
import naga.core.spi.json.teavm.TeaVmJsonFactory;
import naga.core.spi.plat.Net;
import naga.core.spi.plat.Platform;
import naga.core.spi.plat.Scheduler;

/**
 * @author Bruno Salmon
 */
public class TeaVmPlatform implements Platform {

    public static void register() {
        Platform.register(new TeaVmPlatform());
    }

    private final Net net = new TeaVmNet();
    private final JsonFactory jsonFactory = new TeaVmJsonFactory();
    private final Scheduler scheduler = new TeaVmScheduler();

    @Override
    public Type type() {
        return Type.TEAVM;
    }

    @Override
    public Net net() {
        return net;
    }

    @Override
    public Scheduler scheduler() {
        return scheduler;
    }

    @Override
    public JsonFactory jsonFactory() {
        return jsonFactory;
    }

}
