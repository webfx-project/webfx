package webfx.fxkit.extra.controls.displaydata.chart;

import webfx.fxkit.extra.controls.registry.ExtraControlsRegistry;

/**
 * Describes the area chart that presents data as an area between a series of points connected by straight lines and the axis.
 *
 * See {@link Chart} for an explanation of the data format.
 *
 * @author Bruno Salmon
 */
public final class AreaChart extends Chart {

    static {
        ExtraControlsRegistry.registerAreaChart();
    }
}
