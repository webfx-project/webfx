package naga.providers.toolkit.html.fx.html.viewer;

import naga.commons.util.Strings;
import naga.providers.toolkit.html.util.HtmlUtil;
import naga.toolkit.fx.ext.control.HtmlText;
import naga.toolkit.fx.spi.viewer.base.HtmlTextViewerBase;
import naga.toolkit.fx.spi.viewer.base.HtmlTextViewerMixin;

/**
 * @author Bruno Salmon
 */
public class HtmlHtmlTextViewer
        <N extends HtmlText, NV extends HtmlTextViewerBase<N, NV, NM>, NM extends HtmlTextViewerMixin<N, NV, NM>>
        extends HtmlRegionViewer<N, NV, NM>
        implements HtmlTextViewerMixin<N, NV, NM>, HtmlLayoutMeasurable {

    public HtmlHtmlTextViewer() {
        this((NV) new HtmlTextViewerBase());
    }

    HtmlHtmlTextViewer(NV base) {
        super(base, HtmlUtil.createSpanElement());
    }

    @Override
    public void updateText(String text) {
        getElement().innerHTML = Strings.toSafeString(text);
    }

    @Override
    public double minWidth(double height) {
        return 0;
    }
}