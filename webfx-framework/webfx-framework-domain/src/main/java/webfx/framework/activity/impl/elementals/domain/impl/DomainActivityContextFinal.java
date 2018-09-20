package webfx.framework.activity.impl.elementals.domain.impl;

import webfx.framework.activity.ActivityContext;
import webfx.framework.activity.ActivityContextFactory;

/**
 * @author Bruno Salmon
 */
public final class DomainActivityContextFinal extends DomainActivityContextBase<DomainActivityContextFinal> {

    public DomainActivityContextFinal(ActivityContext parentContext, ActivityContextFactory<DomainActivityContextFinal> contextFactory) {
        super(parentContext, contextFactory);
    }

}
