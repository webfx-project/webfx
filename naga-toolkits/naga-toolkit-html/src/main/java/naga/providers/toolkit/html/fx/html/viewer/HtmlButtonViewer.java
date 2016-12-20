package naga.providers.toolkit.html.fx.html.viewer;

import elemental2.HTMLElement;
import naga.providers.toolkit.html.util.HtmlUtil;
import naga.toolkit.fx.scene.control.Button;
import naga.toolkit.fx.spi.viewer.base.ButtonViewerBase;
import naga.toolkit.fx.spi.viewer.base.ButtonViewerMixin;

/**
 * @author Bruno Salmon
 */
public class HtmlButtonViewer
        <N extends Button, NB extends ButtonViewerBase<N, NB, NM>, NM extends ButtonViewerMixin<N, NB, NM>>

        extends HtmlButtonBaseViewer<N, NB, NM>
        implements ButtonViewerMixin<N, NB, NM>, HtmlLayoutMeasurable {

    public HtmlButtonViewer() {
        this((NB) new ButtonViewerBase(), HtmlUtil.createButtonElement());
    }

    public HtmlButtonViewer(NB base, HTMLElement element) {
        super(base, element);
    }
}
