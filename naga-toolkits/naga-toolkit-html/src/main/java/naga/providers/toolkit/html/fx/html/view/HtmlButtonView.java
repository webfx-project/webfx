package naga.providers.toolkit.html.fx.html.view;

import naga.providers.toolkit.html.util.HtmlUtil;
import naga.toolkit.fx.scene.control.Button;
import naga.toolkit.fx.spi.view.base.ButtonViewBase;
import naga.toolkit.fx.spi.view.base.ButtonViewMixin;

/**
 * @author Bruno Salmon
 */
public class HtmlButtonView
        extends HtmlButtonBaseView<Button, ButtonViewBase, ButtonViewMixin>
        implements ButtonViewMixin, HtmlLayoutMeasurable {

    public HtmlButtonView() {
        super(new ButtonViewBase(), HtmlUtil.createButtonElement());
    }
}
