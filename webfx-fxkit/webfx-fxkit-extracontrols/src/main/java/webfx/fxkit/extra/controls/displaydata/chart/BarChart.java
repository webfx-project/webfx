package webfx.fxkit.extra.controls.displaydata.chart;

import webfx.fxkit.extra.controls.registry.ExtraControlsRegistry;

/**
 * Describes the bar chart, a two-axis chart that presents discrete data with rectangular bars.
 *
 * See {@link Chart} for an explanation of the data format.
 *
 * @author Bruno Salmon
 */

public final class BarChart extends Chart {

    static {
        ExtraControlsRegistry.registerBarChart();
    }
}
