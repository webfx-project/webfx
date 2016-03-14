package naga.core.rx;

import naga.core.spi.gui.GuiToolkit;
import rx.Scheduler;
import rx.Subscription;
import rx.functions.Action0;
import rx.subscriptions.Subscriptions;

/**
 * @author Bruno Salmon
 */
public final class UiSubscriptions {

    private UiSubscriptions() {
        // no instance
    }

    /**
     * Create an Subscription that always runs <code>unsubscribe</code> in the event dispatch thread.
     *
     * @param unsubscribe the action to be performed in the ui thread at un-subscription
     * @return an Subscription that always runs <code>unsubscribe</code> in the event dispatch thread.
     */
    public static Subscription unsubscribeInUiThread(Action0 unsubscribe) {
        return Subscriptions.create(() -> {
            if (GuiToolkit.isUiThread())
                unsubscribe.call();
            else {
                Scheduler.Worker worker = RxScheduler.UI_SCHEDULER.createWorker();
                worker.schedule(() -> {
                    unsubscribe.call();
                    worker.unsubscribe();
                });
            }
        });
    }
}

