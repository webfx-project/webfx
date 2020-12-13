package javafx.scene.control;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ObjectPropertyBase;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.skin.TextFieldSkin;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.text.TextAlignment;
import javafx.scene.paint.Color;
import dev.webfx.kit.mapper.peers.javafxgraphics.markers.HasAlignmentProperty;
import dev.webfx.kit.mapper.peers.javafxgraphics.markers.HasTextAlignmentProperty;
import dev.webfx.kit.registry.javafxcontrols.JavaFxControlsRegistry;

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

    /**
     * The action handler associated with this text field, or
     * {@code null} if no action handler is assigned.
     *
     * The action handler is normally called when the user types the ENTER key.
     */
    private ObjectProperty<EventHandler<ActionEvent>> onAction = new ObjectPropertyBase<EventHandler<ActionEvent>>() {
        @Override
        protected void invalidated() {
            setEventHandler(ActionEvent.ACTION, get());
        }

        @Override
        public Object getBean() {
            return TextField.this;
        }

        @Override
        public String getName() {
            return "onAction";
        }
    };
    public final ObjectProperty<EventHandler<ActionEvent>> onActionProperty() { return onAction; }
    public final EventHandler<ActionEvent> getOnAction() { return onActionProperty().get(); }
    public final void setOnAction(EventHandler<ActionEvent> value) { onActionProperty().set(value); }

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

    static {
        JavaFxControlsRegistry.registerTextField();
    }

}
