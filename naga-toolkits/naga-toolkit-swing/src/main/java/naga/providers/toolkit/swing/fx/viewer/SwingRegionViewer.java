package naga.providers.toolkit.swing.fx.viewer;

import naga.toolkit.fx.scene.layout.Region;
import naga.toolkit.fx.spi.viewer.base.RegionViewerBase;
import naga.toolkit.fx.spi.viewer.base.RegionViewerMixin;

import javax.swing.*;

/**
 * @author Bruno Salmon
 */
public abstract class SwingRegionViewer
        <N extends Region, NV extends RegionViewerBase<N, NV, NM>, NM extends RegionViewerMixin<N, NV, NM>>

        extends SwingNodeViewer<N, NV, NM>
        implements RegionViewerMixin<N, NV, NM> {


    SwingRegionViewer(NV base) {
        super(base);
    }

    @Override
    public void updateWidth(Double width) {
        updateSize();
    }

    @Override
    public void updateHeight(Double height) {
        updateSize();
    }

    protected void updateSize() {
        N node = getNode();
        updateSize(node.getWidth().intValue(), node.getHeight().intValue());
    }

    protected void updateSize(int width, int height) {
        if (this instanceof SwingEmbedComponentViewer) {
            JComponent component = ((SwingEmbedComponentViewer) this).getSwingComponent();
            component.setSize(width, height);
        }
    }
}
