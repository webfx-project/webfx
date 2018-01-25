package naga.framework.ui.controls;

/**
 * @author Bruno Salmon
 */
public interface DialogCallback {

    void closeDialog();

    boolean isDialogClosed();

    void showException(Throwable e);

    void addCloseHook(Runnable closeHook);

}
