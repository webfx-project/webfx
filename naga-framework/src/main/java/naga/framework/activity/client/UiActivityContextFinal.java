package naga.framework.activity.client;

import naga.platform.activity.ActivityContext;
import naga.platform.activity.ActivityContextFactory;

/**
 * @author Bruno Salmon
 */
public final class UiActivityContextFinal extends UiActivityContextExtendable<UiActivityContextFinal> {

    public UiActivityContextFinal(ActivityContext parentContext, ActivityContextFactory<UiActivityContextFinal> contextFactory) {
        super(parentContext, contextFactory);
    }
}
