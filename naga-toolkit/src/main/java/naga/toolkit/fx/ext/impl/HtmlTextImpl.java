package naga.toolkit.fx.ext.impl;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import naga.toolkit.fx.ext.control.HtmlText;
import naga.toolkit.fx.geom.BaseBounds;
import naga.toolkit.fx.geom.BoxBounds;
import naga.toolkit.fx.geom.transform.BaseTransform;
import naga.toolkit.fx.scene.layout.impl.RegionImpl;

/**
 * @author Bruno Salmon
 */
public class HtmlTextImpl extends RegionImpl implements HtmlText {

    public HtmlTextImpl() {
    }

    public HtmlTextImpl(String text) {
        setText(text);
    }

    private final Property<String> textProperty = new SimpleObjectProperty<>();
    @Override
    public Property<String> textProperty() {
        return textProperty;
    }

    @Override
    public BaseBounds impl_computeGeomBounds(BaseBounds bounds, BaseTransform tx) {
        System.out.println("Warning: HtmlTextImpl.impl_computeGeomBounds() not implemented");
        return new BoxBounds();
    }
}
