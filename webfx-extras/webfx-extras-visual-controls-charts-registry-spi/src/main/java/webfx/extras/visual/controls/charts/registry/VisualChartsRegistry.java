package webfx.extras.visual.controls.charts.registry;

import webfx.extras.visual.controls.charts.registry.spi.VisualChartsRegistryProvider;
import webfx.platform.shared.util.serviceloader.SingleServiceProvider;

import java.util.ServiceLoader;

public class VisualChartsRegistry {

    private static VisualChartsRegistryProvider getProvider() {
        return SingleServiceProvider.getProvider(VisualChartsRegistryProvider.class, () -> ServiceLoader.load(VisualChartsRegistryProvider.class));
    }

    public static void registerLineChart() {
        getProvider().registerLineChart();
    }

    public static void registerAreaChart() {
        getProvider().registerAreaChart();
    }

    public static void registerBarChart() {
        getProvider().registerBarChart();
    }

    public static void registerPieChart() {
        getProvider().registerPieChart();
    }

    public static void registerScatterChart() {
        getProvider().registerScatterChart();
    }
}
