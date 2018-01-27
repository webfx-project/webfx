package naga.framework.ui.controls;

/**
 * @author Bruno Salmon
 */
public interface DialogCallback {

    void closeDialog();

    boolean isDialogClosed();

    void showException(Throwable e);

    DialogCallback addCloseHook(Runnable closeHook);

}
