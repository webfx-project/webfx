package naga.providers.toolkit.html.fx.html.viewer;

import naga.providers.toolkit.html.util.HtmlUtil;
import naga.toolkit.fx.scene.control.Button;
import naga.toolkit.fx.spi.viewer.base.ButtonViewerBase;
import naga.toolkit.fx.spi.viewer.base.ButtonViewerMixin;

/**
 * @author Bruno Salmon
 */
public class HtmlButtonViewer
        extends HtmlButtonBaseViewer<Button, ButtonViewerBase, ButtonViewerMixin>
        implements ButtonViewerMixin, HtmlLayoutMeasurable {

    public HtmlButtonViewer() {
        super(new ButtonViewerBase(), HtmlUtil.createButtonElement());
    }
}
