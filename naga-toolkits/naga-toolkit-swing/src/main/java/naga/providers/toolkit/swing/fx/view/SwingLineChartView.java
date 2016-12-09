package naga.providers.toolkit.swing.fx.view;

import naga.providers.toolkit.swing.nodes.charts.XChartJPanel;
import naga.toolkit.fx.ext.chart.LineChart;
import naga.toolkit.fx.spi.view.base.LineChartViewBase;
import naga.toolkit.fx.spi.view.base.LineChartViewMixin;
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
public class SwingLineChartView
        extends SwingChartView<LineChart, LineChartViewBase<XChartJPanel>, LineChartViewMixin<XChartJPanel>>
        implements LineChartViewMixin<XChartJPanel> {

    public SwingLineChartView() {
        super(new LineChartViewBase<>());
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
