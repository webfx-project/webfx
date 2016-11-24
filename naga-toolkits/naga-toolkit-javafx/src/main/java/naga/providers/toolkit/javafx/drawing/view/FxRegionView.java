package naga.providers.toolkit.javafx.drawing.view;

import naga.toolkit.drawing.layout.Region;

/**
 * @author Bruno Salmon
 */
public class FxRegionView<N extends Region> extends FxNodeViewImpl<N, javafx.scene.layout.Region> {

    @Override
    javafx.scene.layout.Region createFxNode(N node) {
        return new javafx.scene.layout.Region();
    }
}
