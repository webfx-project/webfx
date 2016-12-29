package naga.fxdata.control;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import naga.fx.geom.BaseBounds;
import naga.fx.geom.BoxBounds;
import naga.fx.geom.transform.BaseTransform;
import naga.fx.scene.layout.Region;
import naga.fx.properties.markers.HasTextProperty;

/**
 * @author Bruno Salmon
 */
public class HtmlText extends Region implements HasTextProperty {

    public HtmlText() {
    }

    public HtmlText(String text) {
        setText(text);
    }

    private final Property<String> textProperty = new SimpleObjectProperty<>();
    @Override
    public Property<String> textProperty() {
        return textProperty;
    }

    @Override
    public BaseBounds impl_computeGeomBounds(BaseBounds bounds, BaseTransform tx) {
        System.out.println("Warning: HtmlText.impl_computeGeomBounds() not implemented");
        return new BoxBounds();
    }
}
