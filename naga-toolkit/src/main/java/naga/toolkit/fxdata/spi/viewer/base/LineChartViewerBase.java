package naga.toolkit.fxdata.spi.viewer.base;

import naga.toolkit.fxdata.chart.LineChart;

/**
 * @author Bruno Salmon
 */
public class LineChartViewerBase
        <C, N extends LineChart, NB extends LineChartViewerBase<C, N, NB, NM>, NM extends LineChartViewerMixin<C, N, NB, NM>>

        extends ChartViewerBase<C, N, NB, NM> {
}
