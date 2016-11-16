package naga.providers.toolkit.javafx.drawing.view;

import javafx.scene.Node;
import naga.toolkit.drawing.shapes.EmbedDrawable;
import naga.toolkit.drawing.spi.DrawingRequester;
import naga.toolkit.drawing.spi.view.EmbedDrawableView;

/**
 * @author Bruno Salmon
 */
public class FxEmbedDrawableView extends FxDrawableViewImpl<EmbedDrawable, Node> implements EmbedDrawableView {

    @Override
    public void bind(EmbedDrawable drawable, DrawingRequester drawingRequester) {
        Node node = drawable.getGuiNode().unwrapToNativeNode();
        setAndBindDrawableProperties(drawable, node);
    }

}
