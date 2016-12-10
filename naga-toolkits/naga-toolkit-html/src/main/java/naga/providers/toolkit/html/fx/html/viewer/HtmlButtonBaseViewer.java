package naga.providers.toolkit.html.fx.html.viewer;

import elemental2.HTMLElement;
import naga.toolkit.fx.scene.control.ButtonBase;
import naga.toolkit.fx.spi.viewer.base.ButtonBaseViewerBase;
import naga.toolkit.fx.spi.viewer.base.ButtonBaseViewerMixin;

/**
 * @author Bruno Salmon
 */
abstract class HtmlButtonBaseViewer
        <N extends ButtonBase, NV extends ButtonBaseViewerBase<N, NV, NM>, NM extends ButtonBaseViewerMixin<N, NV, NM>>
        extends HtmlRegionViewer<N, NV, NM>
        implements ButtonBaseViewerMixin<N, NV, NM> {

    HtmlButtonBaseViewer(NV base, HTMLElement element) {
        super(base, element);
    }

    @Override
    public void updateText(String text) {
        setElementTextContent(text);
    }
}
