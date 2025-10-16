package dev.webfx.kit.mapper.peers.javafxcontrols.gwtj2cl.html;

import dev.webfx.kit.mapper.peers.javafxcontrols.base.ProgressBarPeerBase;
import dev.webfx.kit.mapper.peers.javafxcontrols.base.ProgressBarPeerMixin;
import dev.webfx.kit.mapper.peers.javafxgraphics.elemental2.html.HtmlRegionPeer;
import dev.webfx.kit.mapper.peers.javafxgraphics.elemental2.html.layoutmeasurable.HtmlMeasurable;
import dev.webfx.kit.mapper.peers.javafxgraphics.elemental2.util.HtmlUtil;
import elemental2.dom.HTMLElement;
import elemental2.dom.HTMLProgressElement;
import javafx.scene.control.ProgressBar;

/**
 * @author Bruno Salmon
 */
public final class HtmlProgressBarPeer
        <N extends ProgressBar, NB extends ProgressBarPeerBase<N, NB, NM>, NM extends ProgressBarPeerMixin<N, NB, NM>>

        extends HtmlRegionPeer<N, NB, NM>
        implements ProgressBarPeerMixin<N, NB, NM>, HtmlMeasurable {

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
        double progressValue = progress.doubleValue();
        if (progressValue == ProgressBar.INDETERMINATE_PROGRESS || Double.isNaN(progressValue))
            progressElement.removeAttribute("value");
        else
            progressElement.value = progressValue * 100;
    }

}
