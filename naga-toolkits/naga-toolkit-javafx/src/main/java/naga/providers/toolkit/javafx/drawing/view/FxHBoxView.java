package naga.providers.toolkit.javafx.drawing.view;

import javafx.scene.layout.Region;
import naga.toolkit.drawing.shapes.HBox;
import naga.toolkit.drawing.spi.view.HBoxView;

/**
 * @author Bruno Salmon
 */
public class FxHBoxView extends FxNodeViewImpl<HBox, Region> implements HBoxView {

    @Override
    Region createFxNode(HBox node) {
        return new Region();
    }
}
