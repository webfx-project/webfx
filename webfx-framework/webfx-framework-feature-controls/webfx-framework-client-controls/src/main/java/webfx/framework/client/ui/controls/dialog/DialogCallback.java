package webfx.framework.client.ui.controls.dialog;

/**
 * @author Bruno Salmon
 */
public interface DialogCallback {

    void closeDialog();

    boolean isDialogClosed();

    void showException(Throwable e);

    DialogCallback addCloseHook(Runnable closeHook);

}
