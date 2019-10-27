package webfx.extras.visual.controls.charts;

import webfx.extras.visual.controls.charts.registry.VisualChartsRegistry;

/**
 * Describes a chart that represents data in a form of a circle divided into triangular wedges called slices.
 *
 * See {@link VisualChart} for an explanation of the data format.
 *
 * @author Bruno Salmon
 */
public final class VisualPieChart extends VisualChart {

    static {
        VisualChartsRegistry.registerPieChart();
    }
}
