package naga.fx.spi.gwt.html.viewer;

import elemental2.HTMLElement;
import naga.fx.scene.control.Control;
import naga.fx.spi.viewer.base.ControlViewerBase;
import naga.fx.spi.viewer.base.ControlViewerMixin;

/**
 * @author Bruno Salmon
 */
abstract class HtmlControlViewer
        <N extends Control, NB extends ControlViewerBase<N, NB, NM>, NM extends ControlViewerMixin<N, NB, NM>>

        extends HtmlRegionViewer<N, NB, NM>
        implements ControlViewerMixin<N, NB, NM> {

    HtmlControlViewer(NB base, HTMLElement element) {
        super(base, element);
    }

}
