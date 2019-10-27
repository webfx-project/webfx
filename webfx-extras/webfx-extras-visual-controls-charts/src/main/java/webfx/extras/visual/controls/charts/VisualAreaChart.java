package webfx.extras.visual.controls.charts;

import webfx.extras.visual.controls.charts.registry.VisualChartsRegistry;

/**
 * Describes the area chart that presents data as an area between a series of points connected by straight lines and the axis.
 *
 * See {@link VisualChart} for an explanation of the data format.
 *
 * @author Bruno Salmon
 */
public final class VisualAreaChart extends VisualChart {

    static {
        VisualChartsRegistry.registerAreaChart();
    }
}
