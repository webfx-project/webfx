package naga.toolkit.fxdata.control;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import naga.toolkit.fx.geom.BaseBounds;
import naga.toolkit.fx.geom.BoxBounds;
import naga.toolkit.fx.geom.transform.BaseTransform;
import naga.toolkit.fx.scene.layout.Region;
import naga.toolkit.fx.properties.markers.HasTextProperty;

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
