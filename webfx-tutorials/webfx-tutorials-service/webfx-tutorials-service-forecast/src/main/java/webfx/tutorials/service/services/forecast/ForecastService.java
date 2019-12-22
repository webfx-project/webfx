package webfx.tutorials.service.services.forecast;

import webfx.platform.shared.util.async.Future;
import webfx.platform.shared.util.serviceloader.SingleServiceProvider;
import webfx.tutorials.service.services.forecast.spi.ForecastMetrics;
import webfx.tutorials.service.services.forecast.spi.ForecastServiceProvider;

import java.util.Collection;
import java.util.ServiceLoader;

/**
 * @author Bruno Salmon
 */
public final class ForecastService {

    public static ForecastServiceProvider getProvider() {
        return SingleServiceProvider.getProvider(ForecastServiceProvider.class, () -> ServiceLoader.load(ForecastServiceProvider.class));
    }

    public static Future<Collection<ForecastMetrics>> getWeekForecast(String place) {
        return getProvider().getWeekForecast(place);
    }
}
