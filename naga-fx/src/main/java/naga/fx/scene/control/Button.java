package naga.fx.scene.control;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import naga.fx.event.ActionEvent;
import naga.fx.scene.text.TextAlignment;

/**
 * @author Bruno Salmon
 */
public class Button extends ButtonBase {

    {
        // Naga default style
        setTextAlignment(TextAlignment.CENTER);
    }

    public Button() {
    }

    public Button(String text) {
        setText(text);
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
    private Property<Boolean> defaultButton;
    public final void setDefaultButton(boolean value) {
        defaultButtonProperty().setValue(value);
    }
    public final boolean isDefaultButton() {
        return defaultButton == null ? false : defaultButton.getValue();
    }

    public final Property<Boolean> defaultButtonProperty() {
        if (defaultButton == null) {
            defaultButton = new SimpleObjectProperty<>(false)/* {
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
    private Property<Boolean> cancelButton;
    public final void setCancelButton(boolean value) {
        cancelButtonProperty().setValue(value);
    }
    public final boolean isCancelButton() {
        return cancelButton == null ? false : cancelButton.getValue();
    }

    public final Property<Boolean> cancelButtonProperty() {
        if (cancelButton == null) {
            cancelButton = new SimpleObjectProperty<>(false)/* {
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

}
