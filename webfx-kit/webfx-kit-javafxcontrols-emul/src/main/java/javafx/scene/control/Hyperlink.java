package javafx.scene.control;

import javafx.scene.control.skin.HyperlinkSkin;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import dev.webfx.kit.registry.javafxcontrols.JavaFxControlsRegistry;

/**
 * <p>An HTML like label which can be a graphic and/or text which responds to rollovers and clicks.
 * When a hyperlink is clicked/pressed {@link #isVisited} becomes {@code true}.  A Hyperlink behaves
 * just like a {@link Button}.  When a hyperlink is pressed and released
 * a {@link ActionEvent} is sent, and your application can perform some action based on this event.
 * </p>
 *
 * <p>Example:</p>
 * {@code Hyperlink link = new Hyperlink("www.oracle.com"); }
 * @since JavaFX 2.0
 */
public class Hyperlink extends ButtonBase {

    {
        setTextFill(Color.web("0x0096c9ff")); // Resulting color used by default in JavaFX
    }

    /***************************************************************************
     *                                                                         *
     * Constructors                                                            *
     *                                                                         *
     **************************************************************************/

    /**
     * Creates a hyperlink with no label.
     */
    public Hyperlink() {
        initialize();
    }

    /**
     * Create a hyperlink with the specified text as its label.
     *
     * @param text A text string for its label.
     */
    public Hyperlink(String text) {
        super(text);
        initialize();
    }

    /**
     * Create a hyperlink with the specified text and graphic as its label.
     *
     * @param text A text string for its label.
     * @param graphic A graphic for its label
     */
    public Hyperlink(String text, Node graphic) {
        super(text, graphic);
        initialize();
    }

    private void initialize() {
        // Initialize the style class to be 'hyperlink'.
        getStyleClass().setAll(DEFAULT_STYLE_CLASS);
        //setAccessibleRole(AccessibleRole.HYPERLINK);
        // cursor is styleable through css. Calling setCursor
        // makes it look to css like the user set the value and css will not
        // override. Initializing cursor by calling applyStyle with null
        // StyleOrigin ensures that css will be able to override the value.
        //((StyleableProperty<Cursor>)(WritableValue<Cursor>)cursorProperty()).applyStyle(null, Cursor.HAND);
    }

    /***************************************************************************
     *                                                                         *
     * Properties                                                              *
     *                                                                         *
     **************************************************************************/
    /**
     * Indicates whether this link has already been "visited".
     */
    public final Property<Boolean> visitedProperty() {
        if (visited == null) {
            visited = new SimpleObjectProperty<>(false)/* {
                @Override protected void invalidated() {
                    pseudoClassStateChanged(PSEUDO_CLASS_VISITED, get());
                }

                @Override
                public Object getBean() {
                    return Hyperlink.this;
                }

                @Override
                public String getName() {
                    return "visited";
                }
            }*/;
        }
        return visited;
    }
    private Property<Boolean> visited;
    public final void setVisited(boolean value) {
        visitedProperty().setValue(value);
    }
    public final boolean isVisited() {
        return visited == null ? false : visited.getValue();
    }

    /***************************************************************************
     *                                                                         *
     * Methods                                                                 *
     *                                                                         *
     **************************************************************************/

    /**
     * Implemented to invoke the {@link ActionEvent} if one is defined. This
     * function will also {@link #setVisited} to true.
     */
    @Override public void fire() {
        if (!isDisabled()) {
            // Avoid causing an exception in the case that visited was bound
            if (visited == null || !visited.isBound()) {
                setVisited(true);
            }
            fireEvent(new ActionEvent());
        }
    }

    /***************************************************************************
     *                                                                         *
     * Stylesheet Handling                                                     *
     *                                                                         *
     **************************************************************************/

    private static final String DEFAULT_STYLE_CLASS = "hyperlink";
/*
    private static final PseudoClass PSEUDO_CLASS_VISITED =
            PseudoClass.getPseudoClass("visited");
*/


    /** {@inheritDoc} */
    @Override protected Skin<?> createDefaultSkin() {
        return new HyperlinkSkin(this);
    }

     /**
      * Hyperlink uses HAND as the default value for cursor.
      * This method provides a way for css to get the correct initial value.
      * @treatAsPrivate implementation detail
      */
    @Deprecated @Override
    protected /*do not make final*/ Cursor impl_cssGetCursorInitialValue() {
        return Cursor.HAND;
    }


    /***************************************************************************
     *                                                                         *
     * Accessibility handling                                                  *
     *                                                                         *
     **************************************************************************/

/*
    @Override
    public Object queryAccessibleAttribute(AccessibleAttribute attribute, Object... parameters) {
        switch (attribute) {
            case VISITED: return isVisited();
            default: return super.queryAccessibleAttribute(attribute, parameters);
        }
    }
*/

    static {
        JavaFxControlsRegistry.registerHyperlink();
    }
}
