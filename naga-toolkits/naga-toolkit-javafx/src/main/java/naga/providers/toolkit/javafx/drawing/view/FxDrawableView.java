package naga.providers.toolkit.javafx.drawing.view;

import javafx.scene.Node;
import naga.toolkit.drawing.shapes.Drawable;
import naga.toolkit.drawing.spi.view.DrawableView;

/**
 * @author Bruno Salmon
 */
public interface FxDrawableView<D extends Drawable, N extends Node> extends DrawableView<D> {

    N getFxDrawableNode();

}
