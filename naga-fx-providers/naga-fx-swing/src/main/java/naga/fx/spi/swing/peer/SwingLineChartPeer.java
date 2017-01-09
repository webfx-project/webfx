package naga.fx.spi.swing.peer;

import naga.fxdata.chart.LineChart;
import naga.fxdata.spi.peer.base.LineChartPeerBase;
import naga.fxdata.spi.peer.base.LineChartPeerMixin;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.style.Styler;
import org.knowm.xchart.style.XYStyler;

import java.awt.*;

import static org.knowm.xchart.style.colors.XChartSeriesColors.*;

/**
 * @author Bruno Salmon
 */
public class SwingLineChartPeer
        <N extends LineChart, NB extends LineChartPeerBase<XChartJPanel, N, NB, NM>, NM extends LineChartPeerMixin<XChartJPanel, N, NB, NM>>

        extends SwingChartPeer<N, NB, NM>
        implements LineChartPeerMixin<XChartJPanel, N, NB, NM> {

    public SwingLineChartPeer() {
        super((NB) new LineChartPeerBase());
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
