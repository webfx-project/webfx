package naga.framework.activity.base.elementals.domain.impl;

import naga.framework.activity.ActivityContext;
import naga.framework.activity.ActivityContextFactory;

/**
 * @author Bruno Salmon
 */
public final class DomainActivityContextFinal extends DomainActivityContextBase<DomainActivityContextFinal> {

    public DomainActivityContextFinal(ActivityContext parentContext, ActivityContextFactory<DomainActivityContextFinal> contextFactory) {
        super(parentContext, contextFactory);
    }

}
