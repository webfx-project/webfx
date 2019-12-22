package webfx.framework.client.ui.util.paint;

import javafx.scene.paint.Color;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Paint;

/**
 * @author Bruno Salmon
 */
public final class PaintBuilder {

    private String webColor;
    private String topWebColor;
    private String bottomWebColor;
    private String linearGradient;
    private Paint paint;

    public PaintBuilder setWebColor(String webColor) {
        this.webColor = webColor;
        return this;
    }

    public PaintBuilder setTopWebColor(String topWebColor) {
        this.topWebColor = topWebColor;
        return this;
    }

    public PaintBuilder setBottomWebColor(String bottomWebColor) {
        this.bottomWebColor = bottomWebColor;
        return this;
    }

    public PaintBuilder setLinearGradient(String linearGradient) {
        this.linearGradient = linearGradient;
        return this;
    }

    public PaintBuilder setPaint(Paint paint) {
        this.paint = paint;
        return this;
    }

    public Paint build() {
        if (paint == null) {
            if (linearGradient == null && topWebColor != null && bottomWebColor != null)
                linearGradient = "from 0% 0% to 0% 100%, " + topWebColor + " 0%, " + bottomWebColor + " 100%";
            if (linearGradient != null)
                paint = LinearGradient.valueOf(linearGradient);
            else if (webColor != null)
                paint = Color.valueOf(webColor);
        }
        return paint;
    }
}
