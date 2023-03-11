package dev.webfx.kit.mapper.peers.javafxgraphics.openjfx;

import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.event.EventTarget;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.effect.BlendMode;
import javafx.scene.effect.Effect;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.transform.Transform;
import dev.webfx.kit.mapper.peers.javafxgraphics.SceneRequester;
import dev.webfx.kit.mapper.peers.javafxgraphics.base.NodePeerBase;
import dev.webfx.kit.mapper.peers.javafxgraphics.base.NodePeerImpl;
import dev.webfx.kit.mapper.peers.javafxgraphics.base.NodePeerMixin;
import dev.webfx.platform.util.Numbers;

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
        node.getProperties().put("nodePeer", this); // used by HtmlText in webfx-extras-webtext-controls module
        fxNode = createFxNode();
        getNodePeerBase().bind(node, sceneRequester);
        fxNode.addEventFilter(MouseEvent.MOUSE_CLICKED, event -> getNode().fireEvent(toMouseEvent( getNode(), getNode(), event)));
        fxNode.addEventFilter(ActionEvent.ACTION,       event -> getNode().fireEvent(toActionEvent(getNode(), getNode(), event)));
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
    public void updateTranslateX(Number translateX) {
        fxNode.setTranslateX(Numbers.doubleValue(translateX));
    }

    @Override
    public void updateTranslateY(Number translateY) {
        fxNode.setTranslateY(Numbers.doubleValue(translateY));
    }

    @Override
    public void updateScaleX(Number scaleX) {
        fxNode.setScaleX(Numbers.doubleValue(scaleX));
    }

    @Override
    public void updateScaleY(Number scaleX) {
        fxNode.setScaleX(Numbers.doubleValue(scaleX));
    }

    @Override
    public void updateRotate(Number rotate) {
        fxNode.setRotate(Numbers.doubleValue(rotate));
    }

    @Override
    public void updateTransforms(List<Transform> transforms, ListChangeListener.Change<Transform> change) {
        fxNode.getTransforms().setAll(transforms);
    }

    @Override
    public void updateStyleClass(List<String> styleClass, ListChangeListener.Change<String> change) {
    }

    @Override
    public void updateAllNodeTransforms(List<Transform> allNodeTransforms) {
        // never called
    }

    @Override
    public void updateCursor(Cursor cursor) {
        getFxNode().setCursor(cursor);
    }

    @Override
    public void updateOnDragDetected(EventHandler<? super MouseEvent> eventHandler) {
        fxNode.setOnDragDetected(eventHandler);
    }

    @Override
    public void updateOnDragEntered(EventHandler<? super DragEvent> eventHandler) {
        fxNode.setOnDragEntered(eventHandler);
    }

    @Override
    public void updateOnDragDropped(EventHandler<? super DragEvent> eventHandler) {
        fxNode.setOnDragDropped(eventHandler);
    }

    @Override
    public void updateOnDragOver(EventHandler<? super DragEvent> eventHandler) {
        fxNode.setOnDragOver(eventHandler);
    }

    @Override
    public void updateOnDragExited(EventHandler<? super DragEvent> eventHandler) {
        fxNode.setOnDragExited(eventHandler);
    }

    @Override
    public void updateOnDragDone(EventHandler<? super DragEvent> eventHandler) {
        fxNode.setOnDragDone(eventHandler);
    }

    @Override
    public void onListeningTouchEvents(boolean listening) {
    }

    private static MouseEvent toMouseEvent(Object source, EventTarget target, MouseEvent e) {
        return new MouseEvent(source, target, e.getEventType(), e.getX(), e.getY(), e.getScreenX(), e.getScreenY(), null, e.getClickCount(), e.isShiftDown(), e.isControlDown(), e.isAltDown(), e.isMetaDown(), e.isPrimaryButtonDown(), e.isMiddleButtonDown(), e.isSecondaryButtonDown(), e.isSynthesized(), e.isPopupTrigger(), e.isStillSincePress(), null);
    }

    private static ActionEvent toActionEvent(Object source, EventTarget target, ActionEvent e) {
        return new ActionEvent(source, target);
    }
}
