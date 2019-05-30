package webfx.fxkit.extra.controls.registry;

import webfx.fxkit.extra.controls.registry.spi.ExtraControlsRegistryProvider;
import webfx.platform.shared.util.serviceloader.SingleServiceProvider;

import java.util.ServiceLoader;

public class ExtraControlsRegistry {

    private static ExtraControlsRegistryProvider getProvider() {
        return SingleServiceProvider.getProvider(ExtraControlsRegistryProvider.class, () -> ServiceLoader.load(ExtraControlsRegistryProvider.class));
    }

    public static void registerSvgText() {
        getProvider().registerSvgText();
    }

    public static void registerHtmlText() {
        getProvider().registerHtmlText();
    }

    public static void registerHtmlTextEditor() {
        getProvider().registerHtmlTextEditor();
    }

    public static void registerDataGrid() {
        getProvider().registerDataGrid();
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
