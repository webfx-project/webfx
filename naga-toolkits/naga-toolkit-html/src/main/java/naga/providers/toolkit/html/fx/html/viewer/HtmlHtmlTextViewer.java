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
        <N extends HtmlText, NB extends HtmlTextViewerBase<N, NB, NM>, NM extends HtmlTextViewerMixin<N, NB, NM>>
        extends HtmlRegionViewer<N, NB, NM>
        implements HtmlTextViewerMixin<N, NB, NM>, HtmlLayoutMeasurable {

    public HtmlHtmlTextViewer() {
        this((NB) new HtmlTextViewerBase());
    }

    HtmlHtmlTextViewer(NB base) {
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