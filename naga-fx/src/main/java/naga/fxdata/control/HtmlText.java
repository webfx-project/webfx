package naga.fxdata.control;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.BoxBounds;
import com.sun.javafx.geom.transform.BaseTransform;
import javafx.scene.layout.Region;

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
    void setText(String text) {
        textProperty.setValue(text);
    }
    String getText() {
        return textProperty.getValue();
    }

    @Override
    public BaseBounds impl_computeGeomBounds(BaseBounds bounds, BaseTransform tx) {
        System.out.println("Warning: HtmlText.impl_computeGeomBounds() not implemented");
        return new BoxBounds();
    }
}
