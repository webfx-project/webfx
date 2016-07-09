package naga.providers.toolkit.swing.nodes.controls;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import naga.providers.toolkit.swing.nodes.SwingNode;
import naga.toolkit.spi.nodes.controls.SearchBox;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 * @author Bruno Salmon
 */
public class SwingSearchBox extends SwingNode<JTextField> implements SearchBox<JTextField> {

    public SwingSearchBox() {
        this(new JTextField());
    }

    public SwingSearchBox(JTextField search) {
        super(search);
        search.getDocument().addDocumentListener(new DocumentListener() {
            public void changedUpdate(DocumentEvent e) { updateProperty(); }
            public void removeUpdate(DocumentEvent e) { updateProperty(); }
            public void insertUpdate(DocumentEvent e) { updateProperty(); }
            private void updateProperty() {
                textProperty.setValue(search.getText());
            }
        });
        //placeholderProperty.addListener((observable, oldValue, newValue) -> node.setPlaceholder(newValue));
    }

    private final Property<String> textProperty = new SimpleObjectProperty<>();
    @Override
    public Property<String> textProperty() {
        return textProperty;
    }

    private final Property<String> placeholderProperty = new SimpleObjectProperty<>();
    @Override
    public Property<String> placeholderProperty() {
        return placeholderProperty;
    }
}
