package naga.providers.toolkit.javafx.drawing.view;

import naga.toolkit.drawing.scene.Parent;
import naga.toolkit.drawing.spi.DrawingRequester;
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
