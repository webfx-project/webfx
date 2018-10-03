package webfx.framework.client.ui.util.border;

import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import webfx.platform.shared.util.collection.Collections;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Bruno Salmon
 */
public final class BorderBuilder {

    private List<BorderStrokeBuilder> strokeBuilders;
    private List<BorderStroke> strokes;
    private Border border;

    public BorderBuilder setStrokeBuilders(List<BorderStrokeBuilder> strokeBuilders) {
        this.strokeBuilders = strokeBuilders;
        return this;
    }

    public BorderBuilder setStrokeBuilder(BorderStrokeBuilder strokeBuilder) {
        strokeBuilders = null;
        return addStrokeBuilder(strokeBuilder);
    }

    public BorderBuilder addStrokeBuilder(BorderStrokeBuilder strokeBuilder) {
        if (strokeBuilders == null)
            strokeBuilders = new ArrayList<>();
        strokeBuilders.add(strokeBuilder);
        return this;
    }

    public BorderBuilder setStrokes(List<BorderStroke> strokes) {
        this.strokes = strokes;
        return this;
    }

    public BorderBuilder setStroke(BorderStroke stroke) {
        strokes = null;
        return addStroke(stroke);
    }

    public BorderBuilder addStroke(BorderStroke stroke) {
        if (strokes == null)
            strokes = new ArrayList<>();
        strokes.add(stroke);
        return this;
    }

    public BorderBuilder setBorder(Border border) {
        this.border = border;
        return this;
    }

    public Border build() {
        if (border == null) {
            if (strokes == null && strokeBuilders != null)
                strokes = Collections.map(strokeBuilders, BorderStrokeBuilder::build);
            if (strokes != null)
                border = new Border(Collections.toArray(strokes, BorderStroke[]::new));
        }
        return border;
    }
}
