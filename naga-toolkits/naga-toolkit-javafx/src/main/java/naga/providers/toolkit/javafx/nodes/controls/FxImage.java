package naga.providers.toolkit.javafx.nodes.controls;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.image.ImageView;
import naga.providers.toolkit.javafx.FxImageStore;
import naga.providers.toolkit.javafx.nodes.FxNode;

/**
 * @author Bruno Salmon
 */
public class FxImage extends FxNode<ImageView> implements naga.toolkit.spi.nodes.controls.Image<ImageView> {

    public FxImage() {
        this(createImageView());
    }

    public FxImage(ImageView imageView) {
        super(imageView);
        urlProperty.addListener((observable, oldValue, newValue) -> imageView.setImage(FxImageStore.getImage(newValue)));
    }

    private static ImageView createImageView() {
        return new ImageView();
    }

    private final Property<String> urlProperty = new SimpleObjectProperty<>();
    @Override
    public Property<String> urlProperty() {
        return urlProperty;
    }

}
