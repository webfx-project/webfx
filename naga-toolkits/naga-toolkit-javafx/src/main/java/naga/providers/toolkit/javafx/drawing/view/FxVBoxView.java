package naga.providers.toolkit.javafx.drawing.view;

import javafx.scene.layout.Region;
import naga.toolkit.drawing.layout.VBox;
import naga.toolkit.drawing.spi.view.VBoxView;

/**
 * @author Bruno Salmon
 */
public class FxVBoxView extends FxNodeViewImpl<VBox, Region> implements VBoxView {

    @Override
    Region createFxNode(VBox node) {
        return new Region();
    }
}
