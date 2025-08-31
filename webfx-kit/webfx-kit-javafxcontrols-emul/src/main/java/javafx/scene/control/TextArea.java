package javafx.scene.control;

import dev.webfx.kit.registry.javafxcontrols.JavaFxControlsRegistry;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.event.Event;
import javafx.scene.input.MouseEvent;

/**
 * @author Bruno Salmon
 */
public class TextArea extends TextInputControl {

    /**
     * The default value for {@link #prefColumnCountProperty() prefColumnCount}.
     */
    public static final int DEFAULT_PREF_COLUMN_COUNT = 40;

    /**
     * The default value for {@link #prefRowCountProperty() prefRowCount}.
     */
    public static final int DEFAULT_PREF_ROW_COUNT = 10;

    /**
     * Creates a {@code TextField} with empty text content.
     */
    public TextArea() {
        this("");
    }

    /**
     * Creates a {@code TextField} with initial text content.
     *
     * @param text A string for text content.
     */
    public TextArea(String text) {
        //super(new TextFieldContent());
        getStyleClass().add("text-field");
        //setAccessibleRole(AccessibleRole.TEXT_FIELD);
        setText(text);
    }

    /* *************************************************************************
     *                                                                         *
     * Properties                                                              *
     *                                                                         *
     **************************************************************************/

    /**
     * If a run of text exceeds the width of the {@code TextArea},
     * then this variable indicates whether the text should wrap onto
     * another line.
     */
    private BooleanProperty wrapText = new /*Styleable*/SimpleBooleanProperty(false)/* {
        @Override public Object getBean() {
            return TextArea.this;
        }

        @Override public String getName() {
            return "wrapText";
        }

        @Override public CssMetaData<TextArea,Boolean> getCssMetaData() {
            return StyleableProperties.WRAP_TEXT;
        }
    }*/;
    public final BooleanProperty wrapTextProperty() { return wrapText; }
    public final boolean isWrapText() { return wrapText.getValue(); }
    public final void setWrapText(boolean value) { wrapText.setValue(value); }


    /**
     * The preferred number of text columns. This is used for
     * calculating the {@code TextArea}'s preferred width.
     */
    private IntegerProperty prefColumnCount = new /*Styleable*/SimpleIntegerProperty(DEFAULT_PREF_COLUMN_COUNT) {

        private int oldValue = get();

        @Override
        protected void invalidated() {
            int value = get();
            if (value < 0) {
                if (isBound()) {
                    unbind();
                }
                set(oldValue);
                throw new IllegalArgumentException("value cannot be negative.");
            }
            oldValue = value;
        }

        /*@Override public CssMetaData<TextArea,Number> getCssMetaData() {
            return StyleableProperties.PREF_COLUMN_COUNT;
        }

        @Override
        public Object getBean() {
            return TextArea.this;
        }

        @Override
        public String getName() {
            return "prefColumnCount";
        }*/
    };
    public final IntegerProperty prefColumnCountProperty() { return prefColumnCount; }
    public final int getPrefColumnCount() { return prefColumnCount.getValue(); }
    public final void setPrefColumnCount(int value) { prefColumnCount.setValue(value); }


    /**
     * The preferred number of text rows. This is used for calculating
     * the {@code TextArea}'s preferred height.
     */
    private IntegerProperty prefRowCount = new /*Styleable*/SimpleIntegerProperty(DEFAULT_PREF_ROW_COUNT) {

        private int oldValue = get();

        @Override
        protected void invalidated() {
            int value = get();
            if (value < 0) {
                if (isBound()) {
                    unbind();
                }
                set(oldValue);
                throw new IllegalArgumentException("value cannot be negative.");
            }

            oldValue = value;
        }

        /*@Override public CssMetaData<TextArea,Number> getCssMetaData() {
            return StyleableProperties.PREF_ROW_COUNT;
        }

        @Override
        public Object getBean() {
            return TextArea.this;
        }

        @Override
        public String getName() {
            return "prefRowCount";
        }*/
    };
    public final IntegerProperty prefRowCountProperty() { return prefRowCount; }
    public final int getPrefRowCount() { return prefRowCount.getValue(); }
    public final void setPrefRowCount(int value) { prefRowCount.setValue(value); }


    {
        // In WebFX, TextArea doesn't have a skin because it is mapped directly into an HTML TextArea. But we mimic at
        // least the standard JavaFX behavior that it consumes all mouse events.
        addEventHandler(MouseEvent.ANY, Event::consume); // Same as SkinBase.consumeMouseEvents(true)
    }

    static {
        JavaFxControlsRegistry.registerTextArea();
    }
}
