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
        urlProperty.addListener((observable, oldValue, url) -> image.setUrl(url));
        widthProperty.addListener((observable, oldValue, width) -> image.setWidth(toPx(width)));
        heightProperty.addListener((observable, oldValue, height) -> image.setHeight(toPx(height)));
    }

    private static String toPx(Double size) {
        return size == null ? null : size + "px";
    }

    private final Property<String> urlProperty = new SimpleObjectProperty<>();
    @Override
    public Property<String> urlProperty() {
        return urlProperty;
    }

    private final Property<Double> widthProperty = new SimpleObjectProperty<>();
    @Override
    public Property<Double> widthProperty() {
        return widthProperty;
    }

    private final Property<Double> heightProperty = new SimpleObjectProperty<>();
    @Override
    public Property<Double> heightProperty() {
        return heightProperty;
    }
}
