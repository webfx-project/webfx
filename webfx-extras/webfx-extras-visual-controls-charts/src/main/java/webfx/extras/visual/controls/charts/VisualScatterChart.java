package webfx.extras.visual.controls.charts;

import webfx.extras.visual.controls.charts.registry.VisualChartsRegistry;

/**
 * Describes the scatter chart, a two-axis chart that presents its data as a set of points.
 *
 * See {@link VisualChart} for an explanation of the data format.
 *
 * @author Bruno Salmon
 */
public final class VisualScatterChart extends VisualChart {

    static {
        VisualChartsRegistry.registerScatterChart();
    }
}
