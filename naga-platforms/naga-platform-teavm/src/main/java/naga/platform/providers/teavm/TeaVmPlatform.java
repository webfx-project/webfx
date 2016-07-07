package naga.platform.providers.teavm;

import naga.platform.spi.Platform;
import naga.platform.providers.abstr.web.WebPlatform;

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
