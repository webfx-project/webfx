package naga.providers.toolkit.html.drawing.html.view;

import naga.providers.toolkit.html.util.HtmlUtil;
import naga.toolkit.drawing.scene.control.Button;
import naga.toolkit.drawing.spi.view.base.ButtonViewBase;
import naga.toolkit.drawing.spi.view.base.ButtonViewMixin;

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
