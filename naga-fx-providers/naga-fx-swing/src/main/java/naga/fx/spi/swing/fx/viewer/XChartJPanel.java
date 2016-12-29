package naga.fx.spi.swing.fx.viewer;

import org.knowm.xchart.internal.chartpart.Chart;

import javax.swing.*;
import java.awt.*;

/**
 * @author Bruno Salmon
 */
class XChartJPanel extends JPanel {

    private Chart chart;

    void setChart(Chart chart) {
        this.chart = chart;
        repaint();
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (chart != null) {
            Graphics2D g2d = (Graphics2D)g.create();
            chart.paint(g2d, getWidth(), getHeight());
            g2d.dispose();
        }
    }

}
