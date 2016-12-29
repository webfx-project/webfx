package naga.fxdata.spi.viewer.base;

import naga.fx.spi.viewer.base.RegionViewerMixin;
import naga.fxdata.control.HtmlText;

/**
 * @author Bruno Salmon
 */
public interface HtmlTextViewerMixin
        <N extends HtmlText, NB extends HtmlTextViewerBase<N, NB, NM>, NM extends HtmlTextViewerMixin<N, NB, NM>>

        extends RegionViewerMixin<N, NB, NM> {

    void updateText(String text);

}
