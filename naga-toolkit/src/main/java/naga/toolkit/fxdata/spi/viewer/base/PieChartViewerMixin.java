package naga.toolkit.fxdata.spi.viewer.base;

import naga.toolkit.fxdata.chart.PieChart;

/**
 * @author Bruno Salmon
 */
public interface PieChartViewerMixin
        <C, N extends PieChart, NB extends PieChartViewerBase<C, N, NB, NM>, NM extends PieChartViewerMixin<C, N, NB, NM>>

        extends ChartViewerMixin<C, N, NB, NM> {
}
