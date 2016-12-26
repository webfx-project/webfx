package naga.providers.toolkit.html.fx.html.viewer;

import naga.providers.toolkit.html.util.HtmlUtil;
import naga.toolkit.fx.scene.control.Label;
import naga.toolkit.fx.spi.viewer.base.LabelViewerBase;
import naga.toolkit.fx.spi.viewer.base.LabelViewerMixin;

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
