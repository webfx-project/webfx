package emul.javafx.scene.control;

import emul.com.sun.javafx.scene.control.skin.TextFieldSkin;
import emul.javafx.beans.property.Property;
import emul.javafx.beans.property.SimpleObjectProperty;
import emul.javafx.geometry.Insets;
import emul.javafx.geometry.Pos;
import emul.javafx.scene.layout.Background;
import emul.javafx.scene.layout.BackgroundFill;
import emul.javafx.scene.text.TextAlignment;
import emul.javafx.scene.paint.Color;
import webfx.fxkits.core.properties.markers.HasAlignmentProperty;
import webfx.fxkits.core.properties.markers.HasTextAlignmentProperty;

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

    // Setting the default skin
    @Override
    protected Skin<?> createDefaultSkin() {
        return new TextFieldSkin(this);
    }

    private final static Insets INITIAL_PADDING = new Insets(4, 7, 4, 7);
    private final static Background INITIAL_BACKGROUND = new Background(new BackgroundFill(Color.WHITE, null, null));

    {
        setPadding(INITIAL_PADDING);
        setBackground(INITIAL_BACKGROUND);
    }

}
