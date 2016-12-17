package naga.providers.toolkit.swing.fx.viewer;

import naga.toolkit.fx.ext.chart.LineChart;
import naga.toolkit.fx.spi.viewer.base.LineChartViewerBase;
import naga.toolkit.fx.spi.viewer.base.LineChartViewerMixin;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.style.Styler;
import org.knowm.xchart.style.XYStyler;

import java.awt.*;

import static org.knowm.xchart.style.colors.XChartSeriesColors.*;
import static org.knowm.xchart.style.colors.XChartSeriesColors.BLACK;
import static org.knowm.xchart.style.colors.XChartSeriesColors.BROWN;

/**
 * @author Bruno Salmon
 */
public class SwingLineChartViewer
        extends SwingChartViewer<LineChart, LineChartViewerBase<XChartJPanel>, LineChartViewerMixin<XChartJPanel>>
        implements LineChartViewerMixin<XChartJPanel> {

    public SwingLineChartViewer() {
        super(new LineChartViewerBase<>());
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
