package webfx.framework.ui.filter.rx;

import rx.Scheduler;
import rx.Subscription;
import rx.functions.Action0;
import rx.subscriptions.BooleanSubscription;
import rx.subscriptions.CompositeSubscription;
import rx.subscriptions.Subscriptions;
import webfx.platforms.core.services.scheduler.Scheduled;
import webfx.platforms.core.services.scheduler.spi.SchedulerProvider;
import webfx.platforms.core.services.uischeduler.UiScheduler;

import java.util.concurrent.TimeUnit;

import static java.lang.Math.max;

/**
 * Executes work on the JavaFx UI thread.
 * This scheduler should only be used with actions that execute quickly.
 */
public final class RxScheduler extends Scheduler {

    public static RxScheduler BACKGROUND_SCHEDULER = new RxScheduler(webfx.platforms.core.services.scheduler.Scheduler.getProvider());
    public static RxScheduler UI_SCHEDULER = new RxScheduler(UiScheduler.getProvider());

    private final SchedulerProvider schedulerProvider;

    public RxScheduler(SchedulerProvider schedulerProvider) {
        this.schedulerProvider = schedulerProvider;
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
                Scheduled scheduled = schedulerProvider.scheduleDelay(delayMillis, () -> {
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
                schedulerProvider.scheduleDeferred(() -> {
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
