package naga.core.ngui.rx;

import naga.core.spi.platform.Platform;
import naga.core.spi.platform.Scheduled;
import naga.core.spi.toolkit.Toolkit;
import rx.Scheduler;
import rx.Subscription;
import rx.functions.Action0;
import rx.subscriptions.BooleanSubscription;
import rx.subscriptions.CompositeSubscription;
import rx.subscriptions.Subscriptions;

import java.util.concurrent.TimeUnit;

import static java.lang.Math.max;

/**
 * Executes work on the JavaFx UI thread.
 * This scheduler should only be used with actions that execute quickly.
 */
public final class RxScheduler extends Scheduler {

    public static RxScheduler BACKGROUND_SCHEDULER = new RxScheduler(Platform.get().scheduler());
    public static RxScheduler UI_SCHEDULER = new RxScheduler(Toolkit.get().scheduler());

    private final naga.core.spi.platform.Scheduler nagaScheduler;

    public RxScheduler(naga.core.spi.platform.Scheduler nagaScheduler) {
        this.nagaScheduler = nagaScheduler;
    }

    @Override
    public Worker createWorker() {
        return new Worker() {
            private final CompositeSubscription innerSubscription = new CompositeSubscription();

            @Override
            public void unsubscribe() {
                innerSubscription.unsubscribe();
            }

            @Override
            public boolean isUnsubscribed() {
                return innerSubscription.isUnsubscribed();
            }

            @Override
            public Subscription schedule(final Action0 action, long delayTime, TimeUnit unit) {
                BooleanSubscription s = BooleanSubscription.create();

                long delayMillis = unit.toMillis(max(delayTime, 0));
                Scheduled scheduled = nagaScheduler.scheduleDelay(delayMillis, () -> {
                    if (innerSubscription.isUnsubscribed() || s.isUnsubscribed()) {
                        return;
                    }
                    action.call();
                    innerSubscription.remove(s);
                });

                innerSubscription.add(s);

                // wrap for returning so it also removes it from the 'innerSubscription'
                return Subscriptions.create(() -> {
                    scheduled.cancel();
                    s.unsubscribe();
                    innerSubscription.remove(s);
                });
            }

            @Override
            public Subscription schedule(final Action0 action) {
                final BooleanSubscription s = BooleanSubscription.create();
                nagaScheduler.scheduleDeferred(() -> {
                    if (innerSubscription.isUnsubscribed() || s.isUnsubscribed()) {
                        return;
                    }
                    action.call();
                    innerSubscription.remove(s);
                });

                innerSubscription.add(s);
                // wrap for returning so it also removes it from the 'innerSubscription'
                return Subscriptions.create(() -> {
                    s.unsubscribe();
                    innerSubscription.remove(s);
                });
            }
        };
    }

}
