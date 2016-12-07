package naga.providers.toolkit.swing.fx.view;

import naga.toolkit.fx.scene.layout.Region;
import naga.toolkit.fx.spi.view.base.RegionViewBase;
import naga.toolkit.fx.spi.view.base.RegionViewMixin;

import javax.swing.*;

/**
 * @author Bruno Salmon
 */
public abstract class SwingRegionView
        <N extends Region, NV extends RegionViewBase<N, NV, NM>, NM extends RegionViewMixin<N, NV, NM>>

        extends SwingNodeView<N, NV, NM>
        implements RegionViewMixin<N, NV, NM> {


    SwingRegionView(NV base) {
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
        if (this instanceof SwingEmbedComponentView) {
            JComponent component = ((SwingEmbedComponentView) this).getSwingComponent();
            component.setSize(width, height);
        }
    }
}
