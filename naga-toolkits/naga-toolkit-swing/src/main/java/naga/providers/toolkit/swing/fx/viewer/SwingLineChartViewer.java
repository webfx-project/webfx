package naga.providers.toolkit.swing.fx.viewer;

import naga.toolkit.fx.ext.chart.LineChart;
import naga.toolkit.fx.spi.viewer.base.LineChartViewerBase;
import naga.toolkit.fx.spi.viewer.base.LineChartViewerMixin;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.style.Styler;
import org.knowm.xchart.style.XYStyler;

import java.awt.*;

import static org.knowm.xchart.style.colors.XChartSeriesColors.*;

/**
 * @author Bruno Salmon
 */
public class SwingLineChartViewer
        <N extends LineChart, NV extends LineChartViewerBase<XChartJPanel, N, NV, NM>, NM extends LineChartViewerMixin<XChartJPanel, N, NV, NM>>

        extends SwingChartViewer<N, NV, NM>
        implements LineChartViewerMixin<XChartJPanel, N, NV, NM> {

    public SwingLineChartViewer() {
        super((NV) new LineChartViewerBase());
    }

    @Override
    XYChart createChart() {
        XYChart xyChart = new XYChart(100, 100);
        XYStyler styler = xyChart.getStyler();
        styler.setLegendPosition(Styler.LegendPosition.InsideN);
        styler.setSeriesColors(new Color[]{BLUE, RED, ORANGE, PURPLE, GREEN, YELLOW, MAGENTA, PINK, LIGHT_GREY, CYAN, BROWN, BLACK});
        styler.setMarkerSize(0);
        styler.setPlotMargin(0);
        return xyChart;
    }
}
