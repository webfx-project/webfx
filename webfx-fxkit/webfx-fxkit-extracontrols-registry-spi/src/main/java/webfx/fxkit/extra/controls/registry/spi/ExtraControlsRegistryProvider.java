package webfx.fxkit.extra.controls.registry.spi;

public interface ExtraControlsRegistryProvider {

    void registerSvgText();

    void registerHtmlText();

    void registerHtmlTextEditor();

    void registerDataGrid();

    void registerLineChart();

    void registerAreaChart();

    void registerBarChart();

    void registerPieChart();

    void registerScatterChart();
}
