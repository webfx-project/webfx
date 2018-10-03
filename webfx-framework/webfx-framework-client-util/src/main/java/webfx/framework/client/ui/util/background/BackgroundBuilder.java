package webfx.framework.client.ui.util.background;

import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import webfx.platform.shared.util.collection.Collections;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Bruno Salmon
 */
public final class BackgroundBuilder {

    private List<BackgroundFillBuilder> fillBuilders;
    private List<BackgroundFill> fills;
    private Background background;

    public BackgroundBuilder setFillBuilders(List<BackgroundFillBuilder> fillBuilders) {
        this.fillBuilders = fillBuilders;
        return this;
    }

    public BackgroundBuilder setFillBuilder(BackgroundFillBuilder fillBuilder) {
        fillBuilders = null;
        return addFillBuilder(fillBuilder);
    }

    public BackgroundBuilder addFillBuilder(BackgroundFillBuilder fillBuilder) {
        if (fillBuilders == null)
            fillBuilders = new ArrayList<>();
        fillBuilders.add(fillBuilder);
        return this;
    }

    public BackgroundBuilder setFills(List<BackgroundFill> fills) {
        this.fills = fills;
        return this;
    }

    public BackgroundBuilder setFill(BackgroundFill fill) {
        fills = null;
        return addFill(fill);
    }

    public BackgroundBuilder addFill(BackgroundFill fill) {
        if (fills == null)
            fills = new ArrayList<>();
        fills.add(fill);
        return this;
    }

    public BackgroundBuilder setBackground(Background background) {
        this.background = background;
        return this;
    }

    public Background build() {
        if (background == null) {
            if (fills == null && fillBuilders != null)
                fills = Collections.map(fillBuilders, BackgroundFillBuilder::build);
            background = new Background(Collections.toArray(fills, BackgroundFill[]::new));
        }
        return background;
    }
}
