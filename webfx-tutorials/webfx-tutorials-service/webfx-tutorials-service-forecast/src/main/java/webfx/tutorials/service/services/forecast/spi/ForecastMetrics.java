package webfx.tutorials.service.services.forecast.spi;

import java.time.Instant;

/**
 * @author Bruno Salmon
 */
public final class ForecastMetrics {

    public enum SkyState {
        SUNNY,
        LIGHTLY_CLOUDY,
        CLOUDY,
        LIGHTLY_RAINY,
        RAINY,
        HEAVY_RAINY,
        DRY_STORMY,
        STORMY,
        SNOWY,
    }

    private final Instant instant;
    private final int temperature;
    private final SkyState skyState;

    public ForecastMetrics(Instant instant, int temperature, SkyState skyState) {
        this.instant = instant;
        this.temperature = temperature;
        this.skyState = skyState;
    }

    public Instant getInstant() {
        return instant;
    }

    public int getTemperature() {
        return temperature;
    }

    public SkyState getSkyState() {
        return skyState;
    }
}
