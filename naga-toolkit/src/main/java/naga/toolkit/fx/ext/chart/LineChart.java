package naga.toolkit.fx.ext.chart;

import naga.toolkit.fx.ext.chart.impl.LineChartImpl;

/**
 * Describes the line chart, a type of two-axis chart that presents data as a series of points connected by straight lines.
 *
 * See {@link Chart} for an explanation of the data format.
 *
 * @author Bruno Salmon
 */
public interface LineChart extends Chart {

    static LineChart create() {
        return new LineChartImpl();
    }
}
