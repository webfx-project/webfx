package naga.framework.activity.client;

import naga.platform.activity.ActivityContext;
import naga.platform.activity.ActivityContextFactory;

/**
 * @author Bruno Salmon
 */
public final class UiDomainActivityContextFinal extends UiDomainActivityContextExtendable<UiDomainActivityContextFinal> {

    public UiDomainActivityContextFinal(ActivityContext parentContext, ActivityContextFactory<UiDomainActivityContextFinal> contextFactory) {
        super(parentContext, contextFactory);
    }
}
