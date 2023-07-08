package dev.webfx.kit.mapper.peers.javafxcontrols.gwt.html;

import dev.webfx.kit.mapper.peers.javafxcontrols.base.ProgressBarPeerBase;
import dev.webfx.kit.mapper.peers.javafxcontrols.base.ProgressBarPeerMixin;
import dev.webfx.kit.mapper.peers.javafxgraphics.gwt.html.HtmlRegionPeer;
import dev.webfx.kit.mapper.peers.javafxgraphics.gwt.html.layoutmeasurable.HtmlLayoutMeasurable;
import dev.webfx.kit.mapper.peers.javafxgraphics.gwt.util.HtmlUtil;
import elemental2.dom.HTMLElement;
import elemental2.dom.HTMLProgressElement;
import javafx.scene.control.ProgressBar;

/**
 * @author Bruno Salmon
 */
public final class HtmlProgressBarPeer
        <N extends ProgressBar, NB extends ProgressBarPeerBase<N, NB, NM>, NM extends ProgressBarPeerMixin<N, NB, NM>>

        extends HtmlRegionPeer<N, NB, NM>
        implements ProgressBarPeerMixin<N, NB, NM>, HtmlLayoutMeasurable {

    public HtmlProgressBarPeer() {
        this((NB) new ProgressBarPeerBase(), HtmlUtil.createElement("progress"));
    }

    public HtmlProgressBarPeer(NB base, HTMLElement element) {
        super(base, element);
        HTMLProgressElement progressElement = (HTMLProgressElement) getElement();
        progressElement.max = 100;
    }

    @Override
    public void updateProgress(Number progress) {
        HTMLProgressElement progressElement = (HTMLProgressElement) getElement();
        progressElement.value = progress.doubleValue() * 100;
    }

}
