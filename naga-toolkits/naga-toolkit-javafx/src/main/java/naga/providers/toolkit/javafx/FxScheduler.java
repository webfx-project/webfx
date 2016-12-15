package naga.providers.toolkit.javafx;

import com.sun.javafx.tk.TKPulseListener;
import javafx.application.Platform;
import naga.commons.scheduler.impl.UiSchedulerBase;
import naga.toolkit.spi.Toolkit;

/**
 * @author Bruno Salmon
 */
class FxScheduler extends UiSchedulerBase {

    static FxScheduler SINGLETON = new FxScheduler();

    private FxScheduler() {
    }

    @Override
    public void scheduleDeferred(Runnable runnable) {
        Toolkit toolkit = Toolkit.get();
        if (!toolkit.isReady())
            toolkit.onReady(runnable);
        else
            Platform.runLater(runnable);
    }

    @Override
    public boolean isUiThread() {
        return Toolkit.get().isReady() && Platform.isFxApplicationThread();
    }

    private TKPulseListener tkPulseListener;

    @Override
    protected void checkExecuteAnimationPipeIsScheduledForNextAnimationFrame() {
        if (tkPulseListener == null)
            com.sun.javafx.tk.Toolkit.getToolkit().addSceneTkPulseListener(tkPulseListener = this::executeAnimationPipe);
    }

    @Override
    protected void onExecuteAnimationPipeFinished(boolean noMoreAnimationScheduled) {
        if (noMoreAnimationScheduled && tkPulseListener != null) {
            com.sun.javafx.tk.Toolkit.getToolkit().removeSceneTkPulseListener(tkPulseListener);
            tkPulseListener = null;
        }
    }
}
