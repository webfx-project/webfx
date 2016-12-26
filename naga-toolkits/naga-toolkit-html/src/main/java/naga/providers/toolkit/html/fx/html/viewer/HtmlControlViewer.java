package naga.providers.toolkit.html.fx.html.viewer;

import elemental2.HTMLElement;
import naga.toolkit.fx.scene.control.Control;
import naga.toolkit.fx.spi.viewer.base.ControlViewerBase;
import naga.toolkit.fx.spi.viewer.base.ControlViewerMixin;

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
