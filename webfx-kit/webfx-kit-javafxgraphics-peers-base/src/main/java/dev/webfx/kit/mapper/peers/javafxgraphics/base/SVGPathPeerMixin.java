package dev.webfx.kit.mapper.peers.javafxgraphics.base;

import javafx.scene.shape.FillRule;
import javafx.scene.shape.SVGPath;

/**
 * @author Bruno Salmon
 */
public interface SVGPathPeerMixin
        <N extends SVGPath, NB extends SVGPathPeerBase<N, NB, NM>, NM extends SVGPathPeerMixin<N, NB, NM>>

        extends ShapePeerMixin<N, NB, NM> {

    void updateFillRule(FillRule fillRule);

    void updateContent(String content);
}
