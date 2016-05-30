package naga.core.spi.platform.teavm;

import naga.core.spi.platform.Platform;
import naga.core.spi.platform.web.WebPlatform;

/**
 * @author Bruno Salmon
 */
public final class TeaVmPlatform extends WebPlatform {

    public static void register() {
        Platform.register(new TeaVmPlatform());
    }

    public TeaVmPlatform() {
        super(new TeaVmScheduler(), new TeaVmJsonObject(), new TeaVmWebSocketFactory(), TeaVmResourceService.SINGLETON, TeaVmWindowLocation.current(), TeaVmWindowHistory.current());
    }
}
