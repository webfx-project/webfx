package naga.framework.ui.controls;

/**
 * @author Bruno Salmon
 */
public interface DialogCallback {

    void closeDialog();

    void showException(Throwable e);

    void addCloseHook(Runnable closeHook);

}
