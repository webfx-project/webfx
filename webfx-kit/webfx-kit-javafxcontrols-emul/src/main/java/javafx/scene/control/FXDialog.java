package javafx.scene.control;

import javafx.beans.property.*;
import javafx.scene.Node;
import javafx.stage.Modality;
import javafx.stage.StageStyle;
import javafx.stage.Window;

import java.util.List;

abstract class FXDialog {

    /**************************************************************************
     *
     * Static fields
     *
     **************************************************************************/


    /**************************************************************************
     *
     * Private fields
     *
     **************************************************************************/

    protected Object owner;


    /**************************************************************************
     *
     * Constructors
     *
     **************************************************************************/

    protected FXDialog() {
        // pretty much a no-op, but we expect subclasses to call init(...) once
        // they have initialised their abstract property methods.
    }



    /**************************************************************************
     *
     * Public API
     *
     **************************************************************************/

    public boolean requestPermissionToClose(final Dialog<?> dialog) {
        // We only allow the dialog to be closed abnormally (i.e. via the X button)
        // when there is a cancel button in the dialog, or when there is only
        // one button in the dialog. In all other cases, we disable the ability
        // (as best we can) to close a dialog abnormally.
        boolean denyClose = true;

        // if we are here, the close was abnormal, so we must call close to
        // clean up, if we don't consume the event to cancel closing...
        DialogPane dialogPane = dialog.getDialogPane();
        if (dialogPane != null) {
            List<ButtonType> buttons = dialogPane.getButtonTypes();
            if (buttons.size() == 1) {
                denyClose = false;
            } else {
                // look for cancel button type
                for (ButtonType button : buttons) {
                    if (button == null) continue;

                    ButtonBar.ButtonData type = button.getButtonData();
                    if (type == null) continue;

                    // refer to the comments in close() - we support both CANCEL_CLOSE
                    // and isCancelButton() for allowing a dialog to close in
                    // abnormal circumstances. This allows for consistency with
                    // the ESC key being pressed (which triggers the cancel button
                    // being pressed)
                    if (type == ButtonBar.ButtonData.CANCEL_CLOSE || type.isCancelButton()) {
                        denyClose = false;
                        break;
                    }
                }
            }
        }

        return !denyClose;
    }


    /***************************************************************************
     *
     * Abstract API
     *
     **************************************************************************/

    public abstract void show();

    public abstract void showAndWait();

    // This should only be called from Dialog - it should never be called by
    // subclasses of FXDialog. Implementations should never call up to
    // Dialog.close().
    public abstract void close();

    public abstract void initOwner(Window owner);

    public abstract Window getOwner();

    public abstract void initModality(Modality modality);

    public abstract Modality getModality();

    public abstract ReadOnlyBooleanProperty showingProperty();

    public abstract Window getWindow();

    public abstract void sizeToScene();

    // --- x
    public abstract double getX();
    public abstract void setX(double x);
    public abstract ReadOnlyDoubleProperty xProperty();

    // --- y
    public abstract double getY();
    public abstract void setY(double y);
    public abstract ReadOnlyDoubleProperty yProperty();

    // --- resizable
    abstract BooleanProperty resizableProperty();


    // --- focused
    abstract ReadOnlyBooleanProperty focusedProperty();


    // --- title
    abstract Property<String> titleProperty();

    // --- content
    public abstract void setDialogPane(DialogPane node);

    // --- root
    public abstract Node getRoot();


    // --- width
    /**
     * Property representing the width of the dialog.
     */
    abstract ReadOnlyDoubleProperty widthProperty();

    abstract void setWidth(double width);


    // --- height
    /**
     * Property representing the height of the dialog.
     */
    abstract ReadOnlyDoubleProperty heightProperty();

    abstract void setHeight(double height);


    // stage style
    abstract void initStyle(StageStyle style);
    abstract StageStyle getStyle();


    abstract double getSceneHeight();

    /***************************************************************************
     *
     * Implementation
     *
     **************************************************************************/




    /***************************************************************************
     *
     * Support Classes
     *
     **************************************************************************/



    /***************************************************************************
     *
     * Stylesheet Handling
     *
     **************************************************************************/
}
