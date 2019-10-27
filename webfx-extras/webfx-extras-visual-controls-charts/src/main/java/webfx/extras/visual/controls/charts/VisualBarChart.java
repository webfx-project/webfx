package webfx.extras.visual.controls.charts;

import webfx.extras.visual.controls.charts.registry.VisualChartsRegistry;

/**
 * Describes the bar chart, a two-axis chart that presents discrete data with rectangular bars.
 *
 * See {@link VisualChart} for an explanation of the data format.
 *
 * @author Bruno Salmon
 */

public final class VisualBarChart extends VisualChart {

    static {
        VisualChartsRegistry.registerBarChart();
    }
}
