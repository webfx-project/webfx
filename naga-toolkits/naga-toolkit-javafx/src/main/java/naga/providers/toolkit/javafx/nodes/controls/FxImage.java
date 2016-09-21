package naga.providers.toolkit.javafx.nodes.controls;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.image.ImageView;
import naga.commons.util.Numbers;
import naga.providers.toolkit.javafx.nodes.FxNode;
import naga.providers.toolkit.javafx.util.FxImageStore;

/**
 * @author Bruno Salmon
 */
public class FxImage extends FxNode<ImageView> implements naga.toolkit.spi.nodes.controls.Image<ImageView> {

    public FxImage() {
        this(createImageView());
    }

    public FxImage(ImageView imageView) {
        super(imageView);
        urlProperty.addListener((observable, oldUrl, url) -> imageView.setImage(FxImageStore.getImage(url, Numbers.doubleValue(getWidth()), Numbers.doubleValue(getHeight()))));
    }

    private static ImageView createImageView() {
        return new ImageView();
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
