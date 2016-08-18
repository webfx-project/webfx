package naga.providers.toolkit.gwt.nodes.controls;

import com.google.gwt.user.client.ui.Image;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import naga.providers.toolkit.gwt.nodes.GwtNode;

/**
 * @author Bruno Salmon
 */
public class GwtImage extends GwtNode<Image> implements naga.toolkit.spi.nodes.controls.Image<Image> {

    public GwtImage() {
        this(new Image());
    }

    public GwtImage(Image image) {
        super(image);
        urlProperty.addListener((observable, oldValue, newValue) -> image.setUrl(newValue));
    }

    private final Property<String> urlProperty = new SimpleObjectProperty<>();
    @Override
    public Property<String> urlProperty() {
        return urlProperty;
    }

}
