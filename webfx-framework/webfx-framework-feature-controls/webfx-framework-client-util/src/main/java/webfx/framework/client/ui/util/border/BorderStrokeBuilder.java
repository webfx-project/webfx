package webfx.framework.client.ui.util.border;

import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Paint;
import webfx.framework.client.ui.util.paint.PaintBuilder;

/**
 * @author Bruno Salmon
 */
public final class BorderStrokeBuilder {

    private PaintBuilder strokeBuilder;
    private Paint stroke;
    private BorderStrokeStyle style = BorderStrokeStyle.SOLID;
    private double radius;
    private CornerRadii radii;
    private double width;
    private BorderWidths widths;

    public BorderStrokeBuilder setStrokeBuilder(PaintBuilder strokeBuilder) {
        this.strokeBuilder = strokeBuilder;
        return this;
    }

    public BorderStrokeBuilder setStroke(Paint stroke) {
        this.stroke = stroke;
        return this;
    }

    public BorderStrokeBuilder setStyle(BorderStrokeStyle style) {
        this.style = style;
        return this;
    }

    public BorderStrokeBuilder setRadius(double radius) {
        this.radius = radius;
        return this;
    }

    public BorderStrokeBuilder setRadii(CornerRadii radii) {
        this.radii = radii;
        return this;
    }

    public BorderStrokeBuilder setWidth(double width) {
        this.width = width;
        return this;
    }

    public BorderStrokeBuilder setWidths(BorderWidths widths) {
        this.widths = widths;
        return this;
    }

    public BorderStroke build() {
        if (stroke == null && strokeBuilder != null)
            stroke = strokeBuilder.build();
        if (radii == null && radius > 0)
            radii = new CornerRadii(radius);
        if (widths == null)
            widths = width > 0 ? new BorderWidths(width) : BorderStroke.THIN;
        return new BorderStroke(stroke, style, radii, widths);
    }
}
