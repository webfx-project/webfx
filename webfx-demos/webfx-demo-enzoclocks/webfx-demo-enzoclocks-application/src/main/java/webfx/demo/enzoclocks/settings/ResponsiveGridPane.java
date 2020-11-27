package webfx.demo.enzoclocks.settings;

import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

import java.util.List;

/**
 * @author Bruno Salmon
 */
public class ResponsiveGridPane extends Pane {

    private final static Insets MARGIN = new Insets(10);
    private boolean square;

    public ResponsiveGridPane() {
    }

    public ResponsiveGridPane(Node... children) {
        super(children);
    }

    {
        setBackground(new Background(new BackgroundFill(Color.grayRgb(128, 0.9), null, null)));
    }

    public void setSquare(boolean square) {
        this.square = square;
    }

    @Override
    protected void layoutChildren() {
        List<Node> children = getManagedChildren();
        double w = getWidth()  - MARGIN.getLeft() - MARGIN.getRight();
        double h = getHeight() - MARGIN.getTop()  - MARGIN.getBottom();
        int n = children.size();
        double bestc = 0, bestd = n;
        int bestp = 1, bestq = 1;
        for (int p = 1; p <= n; p++) {
            int q = n / p;
            if (p * q < n)
                q++;
            int d = p * q - n;
            if (d <= bestd) {
                double c = Math.min(h/q, w/p);
                if (d < bestd || c > bestc) {
                    bestc = c;
                    bestd = d;
                    bestp = p;
                    bestq = q;
                }
            }
        }
        int p = bestp, q = bestq;
        double wp = w / p, wx = 0, hp = h / q, hx = 0;
        if (square) {
            wx = (wp - bestc) * p / (p + 1);
            hx = (hp - bestc) * q / (q + 1);
            wp = hp = bestc;
        }
        for (int i = 0; i < n; i++) {
            int col = i % p, row = i / p;
            layoutInArea(children.get(i), MARGIN.getLeft() + wx + col * (wp + wx), MARGIN.getTop() + hx + row * (hp + hx), wp, hp, 0, MARGIN, HPos.CENTER, VPos.CENTER);
        }
    }
}
