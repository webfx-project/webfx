package naga.toolkit.fx.spi.viewer.base;

import naga.toolkit.fx.ext.control.HtmlText;

/**
 * @author Bruno Salmon
 */
public interface HtmlTextViewerMixin
        <N extends HtmlText, NV extends HtmlTextViewerBase<N, NV, NM>, NM extends HtmlTextViewerMixin<N, NV, NM>>

        extends RegionViewerMixin<N, NV, NM> {

    void updateText(String text);

}
