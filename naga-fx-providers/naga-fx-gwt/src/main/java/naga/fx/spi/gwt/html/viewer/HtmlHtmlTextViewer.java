package naga.fx.spi.gwt.html.viewer;

import naga.commons.util.Strings;
import naga.fx.spi.gwt.util.HtmlUtil;
import naga.fxdata.control.HtmlText;
import naga.fxdata.spi.viewer.base.HtmlTextViewerBase;
import naga.fxdata.spi.viewer.base.HtmlTextViewerMixin;

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