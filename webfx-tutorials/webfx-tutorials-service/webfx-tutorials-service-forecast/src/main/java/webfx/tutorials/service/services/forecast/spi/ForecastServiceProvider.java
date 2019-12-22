package webfx.tutorials.service.services.forecast.spi;

import webfx.platform.shared.util.async.Future;

import java.util.Collection;

/**
 * @author Bruno Salmon
 */
public interface ForecastServiceProvider {

    Future<Collection<ForecastMetrics>> getWeekForecast(String place);

}
