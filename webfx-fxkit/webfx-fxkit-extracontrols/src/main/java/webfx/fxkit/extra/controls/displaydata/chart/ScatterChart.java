package webfx.fxkit.extra.controls.displaydata.chart;

import webfx.fxkit.extra.controls.registry.ExtraControlsRegistry;

/**
 * Describes the scatter chart, a two-axis chart that presents its data as a set of points.
 *
 * See {@link Chart} for an explanation of the data format.
 *
 * @author Bruno Salmon
 */
public final class ScatterChart extends Chart {

    static {
        ExtraControlsRegistry.registerScatterChart();
    }
}
