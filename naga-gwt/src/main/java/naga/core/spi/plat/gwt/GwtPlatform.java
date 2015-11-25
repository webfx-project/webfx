package naga.core.spi.plat.gwt;

import naga.core.spi.json.JsonFactory;
import naga.core.spi.json.gwt.GwtJsonFactory;
import naga.core.spi.plat.Net;
import naga.core.spi.plat.Platform;
import naga.core.spi.plat.Scheduler;

/**
 * @author Bruno Salmon
 */
public final class GwtPlatform implements Platform {

    public static void register() {
        Platform.register(new GwtPlatform());
    }

    private final Net net = new GwtNet();
    private final JsonFactory jsonFactory = new GwtJsonFactory();
    private final Scheduler scheduler = new GwtScheduler();

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
