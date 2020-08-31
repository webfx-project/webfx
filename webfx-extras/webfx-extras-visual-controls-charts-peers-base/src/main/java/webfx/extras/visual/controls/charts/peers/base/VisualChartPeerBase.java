package webfx.extras.visual.controls.charts.peers.base;

import webfx.extras.type.Type;
import webfx.extras.visual.VisualColumn;
import webfx.extras.visual.VisualResult;
import webfx.extras.visual.controls.charts.VisualChart;
import webfx.extras.visual.controls.peers.base.SelectableVisualResultControlPeerBase;
import webfx.platform.shared.util.Strings;

/**
 * @author Bruno Salmon
 */
public abstract class VisualChartPeerBase
        <C, N extends VisualChart, NB extends VisualChartPeerBase<C, N, NB, NM>, NM extends VisualChartPeerMixin<C, N, NB, NM>>

        extends SelectableVisualResultControlPeerBase<C, N, NB, NM> {

    private final boolean isPieChart = this instanceof VisualPieChartPeerBase;

    public void updateResult(VisualResult rs) {
        if (rs == null)
            return;
        int rowCount = rs.getRowCount();
        int columnCount = rs.getColumnCount();
        VisualColumn[] columns = rs.getColumns();
        boolean rowFormat = "series".equals(columns[0].getRole());
        boolean hasXAxis = !isPieChart;
        if (!rowFormat) { /***** Column format - see {@link VisualChart} for format description *****/
            if (isPieChart && rowCount > 1) // ignoring extra rows for pie chart
                rowCount = 1;
            int firstSeriesColumnIndex = hasXAxis ? 1 : 0;
            int seriesCount = columnCount - firstSeriesColumnIndex;
            int pointPerSeriesCount = rowCount;
            Type xType = hasXAxis ? columns[0].getType() : null;
            Type yType = columns[firstSeriesColumnIndex].getType();
            mixin.createChartData(xType, yType, pointPerSeriesCount, seriesCount, seriesIndex -> columns[firstSeriesColumnIndex + seriesIndex].getName());
            for (int pointIndex = 0; pointIndex < pointPerSeriesCount; pointIndex++) {
                if (hasXAxis)
                    mixin.setChartDataX(rs.getValue(pointIndex, 0), pointIndex);
                for (int seriesIndex = 0; seriesIndex < seriesCount; seriesIndex++) {
                    Object yValue = rs.getValue(pointIndex, firstSeriesColumnIndex + seriesIndex);
                    mixin.setChartDataY(yValue, pointIndex, seriesIndex);
                }
            }
        } else {          /***** Row format - see {@link VisualChart} for format description *****/
            if (isPieChart && columnCount > 2) // ignoring extra columns for pie charts
                columnCount = 2;
            int seriesCount = rowCount;
            int pointPerSeriesCount = columnCount - 1;
            Type xType = hasXAxis ? columns[1].getType() : null;
            Type yType = columns[pointPerSeriesCount].getType();
            mixin.createChartData(xType, yType, pointPerSeriesCount, seriesCount, seriesIndex -> Strings.toString(rs.getValue(seriesIndex, 0)));
            for (int pointIndex = 0; pointIndex < pointPerSeriesCount; pointIndex++) {
                if (hasXAxis)
                    mixin.setChartDataX(columns[pointIndex + 1].getName(), pointIndex);
                for (int seriesIndex = 0; seriesIndex < seriesCount; seriesIndex++) {
                    Object yValue = rs.getValue(seriesIndex, pointIndex + 1);
                    mixin.setChartDataY(yValue, pointIndex, seriesIndex);
                }
            }
        }
        mixin.applyChartData();
    }

}
