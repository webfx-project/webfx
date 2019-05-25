package webfx.fxkit.extra.controls.displaydata.chart;

import webfx.fxkit.extra.controls.registry.ExtraControlsRegistry;

/**
 * Describes the line chart, a type of two-axis chart that presents data as a series of points connected by straight lines.
 *
 * See {@link Chart} for an explanation of the data format.
 *
 * @author Bruno Salmon
 */

public final class LineChart extends Chart {

    static {
        ExtraControlsRegistry.registerLineChart();
    }
}
