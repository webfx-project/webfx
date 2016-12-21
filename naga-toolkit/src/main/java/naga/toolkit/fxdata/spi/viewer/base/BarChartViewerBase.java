package naga.toolkit.fxdata.spi.viewer.base;

import naga.toolkit.fxdata.chart.BarChart;

/**
 * @author Bruno Salmon
 */
public class BarChartViewerBase
        <C, N extends BarChart, NB extends BarChartViewerBase<C, N, NB, NM>, NM extends BarChartViewerMixin<C, N, NB, NM>>

        extends ChartViewerBase<C, N, NB, NM> {
}
