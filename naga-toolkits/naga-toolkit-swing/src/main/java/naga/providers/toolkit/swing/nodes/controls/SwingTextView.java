package naga.providers.toolkit.swing.nodes.controls;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import naga.providers.toolkit.swing.nodes.SwingNode;
import naga.providers.toolkit.swing.util.JGradientLabel;
import naga.toolkit.spi.nodes.controls.TextView;

import javax.swing.*;

/**
 * @author Bruno Salmon
 */
public class SwingTextView extends SwingNode<JLabel> implements TextView {

    public SwingTextView() {
        this(new JGradientLabel());
    }

    public SwingTextView(JLabel label) {
        super(label);
        textProperty.addListener((observable, oldValue, newValue) -> node.setText(newValue));
    }

    private final Property<String> textProperty = new SimpleObjectProperty<>();
    @Override
    public Property<String> textProperty() {
        return textProperty;
    }

}
