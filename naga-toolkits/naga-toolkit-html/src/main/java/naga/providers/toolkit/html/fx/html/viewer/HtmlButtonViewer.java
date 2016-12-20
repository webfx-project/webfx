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
        <N extends Button, NV extends ButtonViewerBase<N, NV, NM>, NM extends ButtonViewerMixin<N, NV, NM>>

        extends HtmlButtonBaseViewer<N, NV, NM>
        implements ButtonViewerMixin<N, NV, NM>, HtmlLayoutMeasurable {

    public HtmlButtonViewer() {
        this((NV) new ButtonViewerBase(), HtmlUtil.createButtonElement());
    }

    public HtmlButtonViewer(NV base, HTMLElement element) {
        super(base, element);
    }
}
