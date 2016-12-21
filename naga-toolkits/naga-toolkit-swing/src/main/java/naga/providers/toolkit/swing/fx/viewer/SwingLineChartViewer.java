package naga.providers.toolkit.swing.fx.viewer;

import naga.toolkit.fxdata.chart.LineChart;
import naga.toolkit.fxdata.spi.viewer.base.LineChartViewerBase;
import naga.toolkit.fxdata.spi.viewer.base.LineChartViewerMixin;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.style.Styler;
import org.knowm.xchart.style.XYStyler;

import java.awt.*;

import static org.knowm.xchart.style.colors.XChartSeriesColors.*;

/**
 * @author Bruno Salmon
 */
public class SwingLineChartViewer
        <N extends LineChart, NB extends LineChartViewerBase<XChartJPanel, N, NB, NM>, NM extends LineChartViewerMixin<XChartJPanel, N, NB, NM>>

        extends SwingChartViewer<N, NB, NM>
        implements LineChartViewerMixin<XChartJPanel, N, NB, NM> {

    public SwingLineChartViewer() {
        super((NB) new LineChartViewerBase());
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
