package javafx.scene.control;

import dev.webfx.kit.registry.javafxcontrols.JavaFxControlsRegistry;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.skin.ButtonSkin;

/**
 * @author Bruno Salmon
 */
public class Button extends ButtonBase {

    /**
     * Creates a button with an empty string for its label.
     */
    public Button() {
        //initialize();
    }

    /**
     * Creates a button with the specified text as its label.
     *
     * @param text A text string for its label.
     */
    public Button(String text) {
        super(text);
        //initialize();
    }

    /**
     * Creates a button with the specified text and icon for its label.
     *
     * @param text A text string for its label.
     * @param graphic the icon for its label.
     */
    public Button(String text, Node graphic) {
        super(text, graphic);
        //initialize();
    }


    /***************************************************************************
     *                                                                         *
     * Properties                                                              *
     *                                                                         *
     **************************************************************************/

    /**
     * A default Button is the button that receives
     * a keyboard VK_ENTER press, if no other node in the scene consumes it.
     */
    private BooleanProperty defaultButton;
    public final void setDefaultButton(boolean value) {
        defaultButtonProperty().setValue(value);
    }
    public final boolean isDefaultButton() {
        return defaultButton == null ? false : defaultButton.getValue();
    }

    public final BooleanProperty defaultButtonProperty() {
        if (defaultButton == null) {
            defaultButton = new SimpleBooleanProperty(false)/* {
                @Override protected void invalidated() {
                    pseudoClassStateChanged(PSEUDO_CLASS_DEFAULT, get());
                }

                @Override
                public Object getBean() {
                    return Button.this;
                }

                @Override
                public String getName() {
                    return "defaultButton";
                }
            }*/;
        }
        return defaultButton;
    }


    /**
     * A Cancel Button is the button that receives
     * a keyboard VK_ESC press, if no other node in the scene consumes it.
     */
    private BooleanProperty cancelButton;
    public final void setCancelButton(boolean value) {
        cancelButtonProperty().setValue(value);
    }
    public final boolean isCancelButton() {
        return cancelButton == null ? false : cancelButton.getValue();
    }

    public final BooleanProperty cancelButtonProperty() {
        if (cancelButton == null) {
            cancelButton = new SimpleBooleanProperty(false)/* {
                @Override protected void invalidated() {
                    pseudoClassStateChanged(PSEUDO_CLASS_CANCEL, get());
                }

                @Override
                public Object getBean() {
                    return javafx.scene.control.Button.this;
                }

                @Override
                public String getName() {
                    return "cancelButton";
                }
            }*/;
        }
        return cancelButton;
    }

    /***************************************************************************
     *                                                                         *
     * Methods                                                                 *
     *                                                                         *
     **************************************************************************/

    /** {@inheritDoc} */
    @Override public void fire() {
        if (!isDisabled()) {
            fireEvent(new ActionEvent());
        }
    }

    // Webfx default hardcoded Style to match JavaFX default theme

    @Override
    protected Skin<?> createDefaultSkin() {
        return new ButtonSkin(this);
    }

    /*final static CornerRadii RADII = new CornerRadii(1);
    final static Border BORDER = new Border(new BorderStroke(Color.DARKGRAY, BorderStrokeStyle.SOLID, RADII, BorderWidths.DEFAULT));
    final static Background BACKGROUND = new Background(new BackgroundFill(LinearGradient.valueOf("from 0% 0% to 0% 100%, white 0%, #E0E0E0 100%"), RADII, Insets.EMPTY));
    */
    public final static Insets PADDING = new Insets(6);
    {
        //setBorder(BORDER);
        //setBackground(BACKGROUND);
        setPadding(PADDING);
        setAlignment(Pos.CENTER);
    }

    static {
        JavaFxControlsRegistry.registerButton();
    }
}
