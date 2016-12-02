package naga.toolkit.fx.spi.view.base;

import javafx.beans.property.Property;
import naga.toolkit.fx.scene.control.Labeled;
import naga.toolkit.fx.spi.DrawingRequester;
import naga.toolkit.fx.spi.view.LabeledView;

/**
 * @author Bruno Salmon
 */
public abstract class LabeledViewBase
        <N extends Labeled, NV extends LabeledViewBase<N, NV, NM>, NM extends LabeledViewMixin<N, NV, NM>>

        extends ControlViewBase<N, NV, NM>
        implements LabeledView<N> {

    @Override
    public void bind(N labeled, DrawingRequester drawingRequester) {
        super.bind(labeled, drawingRequester);
        requestUpdateOnPropertiesChange(drawingRequester,
                node.textProperty(),
                node.imageProperty());
    }

    @Override
    public boolean updateProperty(Property changedProperty) {
        N c = getNode();
        return super.updateProperty(changedProperty)
                || updateProperty(c.textProperty(), changedProperty, mixin::updateText)
                //|| updateProperty(c.imageProperty(), changedProperty, mixin::updateImage)
                ;
    }
}
