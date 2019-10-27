package webfx.extras.visual.controls.charts.registry.spi;

public interface VisualChartsRegistryProvider {

    void registerLineChart();

    void registerAreaChart();

    void registerBarChart();

    void registerPieChart();

    void registerScatterChart();
}
