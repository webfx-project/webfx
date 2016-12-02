package naga.providers.toolkit.javafx.fx.view;

import naga.toolkit.fx.scene.layout.Region;

/**
 * @author Bruno Salmon
 */
public class FxRegionView<N extends Region> extends FxNodeViewImpl<N, javafx.scene.layout.Region> {

    @Override
    javafx.scene.layout.Region createFxNode(N node) {
        return new javafx.scene.layout.Region();
    }
}
