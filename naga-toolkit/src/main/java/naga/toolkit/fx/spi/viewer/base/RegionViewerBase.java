package naga.toolkit.fx.spi.viewer.base;

import javafx.beans.value.ObservableValue;
import naga.toolkit.fx.scene.layout.Region;
import naga.toolkit.fx.spi.DrawingRequester;
import naga.toolkit.fx.spi.viewer.RegionViewer;

/**
 * @author Bruno Salmon
 */
public class RegionViewerBase
        <N extends Region, NV extends RegionViewerBase<N, NV, NM>, NM extends RegionViewerMixin<N, NV, NM>>
        extends NodeViewerBase<N, NV, NM>
        implements RegionViewer<N> {

    @Override
    public void bind(N node, DrawingRequester drawingRequester) {
        super.bind(node, drawingRequester);
        requestUpdateOnPropertiesChange(drawingRequester
                , node.widthProperty()
                , node.heightProperty()
        );
    }

    @Override
    public boolean updateProperty(ObservableValue changedProperty) {
        return super.updateProperty(changedProperty)
                || updateProperty(node.widthProperty(), changedProperty, mixin::updateWidth)
                || updateProperty(node.heightProperty(), changedProperty, mixin::updateHeight)
                ;
    }
}
