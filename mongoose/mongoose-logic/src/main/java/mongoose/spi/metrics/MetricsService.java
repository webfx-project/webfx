package mongoose.spi.metrics;

/**
 * @author Bruno Salmon
 */
public interface MetricsService {

    void takeMetricsSnapshot(Metrics metrics);

    static MetricsService get() {
        return MetricsServiceSt.get();
    }

}
