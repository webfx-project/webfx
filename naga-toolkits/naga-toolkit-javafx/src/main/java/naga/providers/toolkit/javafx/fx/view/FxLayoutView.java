package naga.providers.toolkit.javafx.fx.view;

import naga.toolkit.fx.scene.layout.Region;

/**
 * @author Bruno Salmon
 */
public class FxLayoutView extends FxRegionView<Region, javafx.scene.layout.Region> {

    @Override
    javafx.scene.layout.Region createFxNode(Region layoutRegion) {
        return new javafx.scene.layout.Region() {
            @Override
            protected void layoutChildren() {
                // We disable the Region children layout (autosize) as the layout is now done by NagaFx (and not JavaFx)
            }
        };
    }
}
