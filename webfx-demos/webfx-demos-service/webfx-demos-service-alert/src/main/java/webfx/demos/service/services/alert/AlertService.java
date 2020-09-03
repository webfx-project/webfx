package webfx.demos.service.services.alert;

import webfx.platform.shared.util.serviceloader.SingleServiceProvider;
import webfx.demos.service.services.alert.spi.AlertServiceProvider;

import java.util.ServiceLoader;

/**
 * @author Bruno Salmon
 */
public final class AlertService {

    public static AlertServiceProvider getProvider() {
        return SingleServiceProvider.getProvider(AlertServiceProvider.class, () -> ServiceLoader.load(AlertServiceProvider.class));
    }

    public static void alert(String message) {
        getProvider().alert(message);
    }

}
