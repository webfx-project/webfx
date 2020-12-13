package dev.webfx.kit.mapper.peers.javafxgraphics.base;

import javafx.collections.ListChangeListener;
import javafx.scene.shape.FillRule;
import javafx.scene.shape.Path;
import javafx.scene.shape.PathElement;

import java.util.List;

/**
 * @author Bruno Salmon
 */
public interface PathPeerMixin
        <N extends Path, NB extends PathPeerBase<N, NB, NM>, NM extends PathPeerMixin<N, NB, NM>>

        extends ShapePeerMixin<N, NB, NM> {

    void updateFillRule(FillRule fillRule);

    void updateElements(List<PathElement> elements, ListChangeListener.Change<PathElement> change);

}
