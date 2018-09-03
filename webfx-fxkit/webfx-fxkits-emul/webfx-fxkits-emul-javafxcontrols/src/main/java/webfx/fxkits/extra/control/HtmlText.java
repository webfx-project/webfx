package webfx.fxkits.extra.control;

import emul.com.sun.javafx.geom.BaseBounds;
import emul.com.sun.javafx.geom.BoxBounds;
import emul.com.sun.javafx.geom.transform.BaseTransform;
import emul.javafx.beans.property.Property;
import emul.javafx.beans.property.SimpleObjectProperty;
import emul.javafx.scene.layout.Region;

/**
 * @author Bruno Salmon
 */
public class HtmlText extends Region {

    public HtmlText() {
    }

    public HtmlText(String text) {
        setText(text);
    }

    private final Property<String> textProperty = new SimpleObjectProperty<>();
    public Property<String> textProperty() {
        return textProperty;
    }
    public void setText(String text) {
        textProperty.setValue(text);
    }
    public String getText() {
        return textProperty.getValue();
    }

    @Override
    public BaseBounds impl_computeGeomBounds(BaseBounds bounds, BaseTransform tx) {
        System.out.println("Warning: HtmlText.impl_computeGeomBounds() not implemented");
        return new BoxBounds();
    }
}
