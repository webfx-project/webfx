package emul.javafx.scene.control;

import emul.com.sun.javafx.scene.control.skin.TextFieldSkin;
import emul.javafx.beans.property.Property;
import emul.javafx.beans.property.SimpleObjectProperty;
import emul.javafx.geometry.Pos;
import emul.javafx.scene.text.TextAlignment;
import naga.fx.properties.markers.HasAlignmentProperty;
import naga.fx.properties.markers.HasTextAlignmentProperty;

/**
 * @author Bruno Salmon
 */
public class TextField extends TextInputControl implements HasAlignmentProperty, HasTextAlignmentProperty {

    /**
     * Creates a {@code TextField} with empty text content.
     */
    public TextField() {
        this("");
    }

    /**
     * Creates a {@code TextField} with initial text content.
     *
     * @param text A string for text content.
     */
    public TextField(String text) {
        //super(new TextFieldContent());
        getStyleClass().add("text-field");
        //setAccessibleRole(AccessibleRole.TEXT_FIELD);
        setText(text);
    }

    private final Property<Pos> alignmentProperty = new SimpleObjectProperty<>(Pos.CENTER_LEFT);
    @Override
    public Property<Pos> alignmentProperty() {
        return alignmentProperty;
    }

    private final Property<TextAlignment> textAlignmentProperty = new SimpleObjectProperty<>(TextAlignment.LEFT);
    @Override
    public Property<TextAlignment> textAlignmentProperty() {
        return textAlignmentProperty;
    }

    // Setting the default skin (empty as we rely on the target toolkit for now) but this allows to add decorators for validation
    @Override
    protected Skin<?> createDefaultSkin() {
        return new TextFieldSkin(this);
    }

    // We continue to use the target toolkit layout measurable even if there is a skin
    @Override
    protected boolean shouldUseLayoutMeasurable() {
        return true;
    }

}
