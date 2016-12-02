package naga.providers.toolkit.javafx.fx.view;

import naga.toolkit.fx.scene.Parent;
import naga.toolkit.fx.spi.DrawingRequester;
import naga.toolkit.util.ObservableLists;

/**
 * @author Bruno Salmon
 */
abstract class FxParentView<N extends Parent, FxN extends javafx.scene.Parent> extends FxNodeViewImpl<N, FxN> {

    @Override
    public void bind(N node, DrawingRequester drawingRequester) {
        super.bind(node, drawingRequester);
        ObservableLists.runNowAndOnListChange(() -> drawingRequester.requestParentAndChildrenViewsUpdate(node), node.getChildren());
    }

}
