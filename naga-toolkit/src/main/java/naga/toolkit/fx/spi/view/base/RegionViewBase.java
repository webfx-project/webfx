package naga.toolkit.fx.spi.view.base;

import javafx.beans.property.Property;
import naga.toolkit.fx.scene.layout.Region;
import naga.toolkit.fx.spi.DrawingRequester;
import naga.toolkit.fx.spi.view.RegionView;

/**
 * @author Bruno Salmon
 */
public class RegionViewBase
        <N extends Region, NV extends RegionViewBase<N, NV, NM>, NM extends RegionViewMixin<N, NV, NM>>
        extends NodeViewBase<N, NV, NM>
        implements RegionView<N> {

    @Override
    public void bind(N node, DrawingRequester drawingRequester) {
        super.bind(node, drawingRequester);
        requestUpdateOnPropertiesChange(drawingRequester,
                node.widthProperty(),
                node.heightProperty());
    }

    @Override
    public boolean updateProperty(Property changedProperty) {
        return super.updateProperty(changedProperty)
                || updateProperty(node.widthProperty(), changedProperty, mixin::updateWidth)
                || updateProperty(node.heightProperty(), changedProperty, mixin::updateHeight)
                ;
    }
}
