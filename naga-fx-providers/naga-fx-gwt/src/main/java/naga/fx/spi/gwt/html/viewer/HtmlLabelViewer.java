package naga.fx.spi.gwt.html.viewer;

import naga.fx.scene.control.Label;
import naga.fx.spi.gwt.util.HtmlUtil;
import naga.fx.spi.viewer.base.LabelViewerBase;
import naga.fx.spi.viewer.base.LabelViewerMixin;

/**
 * @author Bruno Salmon
 */
public class HtmlLabelViewer
        <N extends Label, NB extends LabelViewerBase<N, NB, NM>, NM extends LabelViewerMixin<N, NB, NM>>
        extends HtmlLabeledViewer<N, NB, NM>
        implements LabelViewerMixin<N, NB, NM>, HtmlLayoutMeasurable {

    public HtmlLabelViewer() {
        this((NB) new LabelViewerBase());
    }

    public HtmlLabelViewer(NB base) {
        super(base, HtmlUtil.createSpanElement());
        setElementStyleAttribute("line-height", "100%");
    }
}
