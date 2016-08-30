package naga.providers.toolkit.swing.nodes.controls;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import naga.providers.toolkit.swing.nodes.SwingNode;
import naga.providers.toolkit.swing.util.SwingImageStore;
import naga.toolkit.spi.nodes.controls.Image;

import javax.swing.*;

/**
 * @author Bruno Salmon
 */
public class SwingImage extends SwingNode<JLabel> implements Image<JLabel> {

    public SwingImage() {
        this(new JLabel());
    }

    public SwingImage(JLabel label) {
        super(label);
        urlProperty.addListener((observable, oldValue, newValue) -> label.setIcon(SwingImageStore.getIcon(newValue)));
    }

    private final Property<String> urlProperty = new SimpleObjectProperty<>();
    @Override
    public Property<String> urlProperty() {
        return urlProperty;
    }

}
