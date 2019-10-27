package webfx.extras.visual.controls.charts;

import webfx.extras.visual.controls.charts.registry.VisualChartsRegistry;

/**
 * Describes the line chart, a type of two-axis chart that presents data as a series of points connected by straight lines.
 *
 * See {@link VisualChart} for an explanation of the data format.
 *
 * @author Bruno Salmon
 */

public final class VisualLineChart extends VisualChart {

    static {
        VisualChartsRegistry.registerLineChart();
    }
}
