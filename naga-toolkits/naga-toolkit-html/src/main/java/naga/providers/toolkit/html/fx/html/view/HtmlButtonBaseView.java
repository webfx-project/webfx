package naga.providers.toolkit.html.fx.html.view;

import elemental2.HTMLElement;
import naga.toolkit.fx.scene.control.ButtonBase;
import naga.toolkit.fx.spi.view.base.ButtonBaseViewBase;
import naga.toolkit.fx.spi.view.base.ButtonBaseViewMixin;

/**
 * @author Bruno Salmon
 */
abstract class HtmlButtonBaseView
        <N extends ButtonBase, NV extends ButtonBaseViewBase<N, NV, NM>, NM extends ButtonBaseViewMixin<N, NV, NM>>
        extends HtmlRegionView<N, NV, NM>
        implements ButtonBaseViewMixin<N, NV, NM> {

    HtmlButtonBaseView(NV base, HTMLElement element) {
        super(base, element);
    }

    @Override
    public void updateText(String text) {
        setElementTextContent(text);
    }
}
