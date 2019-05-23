package webfx.fxkit.javafx.mapper.peer;

import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.event.EventTarget;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.effect.BlendMode;
import javafx.scene.effect.Effect;
import javafx.scene.input.MouseEvent;
import javafx.scene.transform.Transform;
import webfx.fxkit.javafxgraphics.mapper.spi.SceneRequester;
import webfx.fxkit.javafxgraphics.mapper.spi.impl.peer.base.NodePeerBase;
import webfx.fxkit.javafxgraphics.mapper.spi.impl.peer.base.NodePeerImpl;
import webfx.fxkit.javafxgraphics.mapper.spi.impl.peer.base.NodePeerMixin;
import webfx.platform.shared.util.Numbers;

import java.util.List;


/**
 * @author Bruno Salmon
 */
public abstract class FxNodePeer
        <FxN extends javafx.scene.Node, N extends Node, NB extends NodePeerBase<N, NB, NM>, NM extends NodePeerMixin<N, NB, NM>>

        extends NodePeerImpl<N, NB, NM>
        implements NodePeerMixin<N, NB, NM> {

    private FxN fxNode;

    protected FxNodePeer(NB base) {
        super(base);
    }

    public FxN getFxNode() {
        return fxNode;
    }

    protected abstract FxN createFxNode();

    protected void onFxNodeCreated() {
    }

    @Override
    public void bind(N node, SceneRequester sceneRequester) {
        node.getProperties().put("nodePeer", this); // used by HtmlText in webfx-fxkits-core module
        fxNode = createFxNode();
        getNodePeerBase().bind(node, sceneRequester);
        fxNode.addEventFilter(javafx.scene.input.MouseEvent.MOUSE_CLICKED, event -> getNode().fireEvent(toMouseEvent(getNode(), getNode(), event)));
        fxNode.addEventFilter(javafx.event.ActionEvent.ACTION, event -> getNode().fireEvent(toActionEvent(getNode(), getNode(), event)));
        onFxNodeCreated();
    }

    @Override
    public void requestFocus() {
        getFxNode().requestFocus();
    }

    @Override
    public void updateMouseTransparent(Boolean mouseTransparent) {
        getFxNode().setMouseTransparent(mouseTransparent);
    }

    @Override
    public void updateId(String id) {
        fxNode.setId(id);
    }

    @Override
    public void updateVisible(Boolean visible) {
        fxNode.setVisible(visible);
    }

    @Override
    public void updateOpacity(Double opacity) {
        fxNode.setOpacity(opacity);
    }

    @Override
    public void updateDisabled(Boolean disabled) {
        fxNode.setDisable(disabled);
    }

    @Override
    public void updateBlendMode(BlendMode blendMode) {
        fxNode.setBlendMode(blendMode);
    }

    @Override
    public void updateEffect(Effect effect) {
        fxNode.setEffect(effect);
    }

    @Override
    public void updateClip(Node clip) {
        fxNode.setClip(clip);
    }

    @Override
    public void updateLayoutX(Number layoutX) {
        fxNode.setLayoutX(Numbers.doubleValue(layoutX));
    }

    @Override
    public void updateLayoutY(Number layoutY) {
        fxNode.setLayoutY(Numbers.doubleValue(layoutY));
    }

    @Override
    public void updateTransforms(List<Transform> transforms, ListChangeListener.Change<Transform> change) {
        fxNode.getTransforms().setAll(transforms);
    }

    @Override
    public void updateStyleClass(List<String> styleClass, ListChangeListener.Change<String> change) {
    }

    @Override
    public void updateLocalToParentTransforms(List<Transform> localToParentTransforms) {
        // never called
    }

    @Override
    public void updateCursor(Cursor cursor) {
        getFxNode().setCursor(cursor);
    }


    private static MouseEvent toMouseEvent(Object source, EventTarget target, javafx.scene.input.MouseEvent e) {
        return new MouseEvent(source, target, e.getEventType(), e.getX(), e.getY(), e.getScreenX(), e.getScreenY(), null, e.getClickCount(), e.isShiftDown(), e.isControlDown(), e.isAltDown(), e.isMetaDown(), e.isPrimaryButtonDown(), e.isMiddleButtonDown(), e.isSecondaryButtonDown(), e.isSynthesized(), e.isPopupTrigger(), e.isStillSincePress(), null);
    }

    private static ActionEvent toActionEvent(Object source, EventTarget target, javafx.event.ActionEvent e) {
        return new ActionEvent(source, target);
    }
}
