package naga.framework.activity;

import naga.platform.activity.ActivityContext;
import naga.platform.activity.ActivityContextFactory;

/**
 * @author Bruno Salmon
 */
final class DomainActivityContextFinal extends DomainActivityContextBase<DomainActivityContextFinal> {

    DomainActivityContextFinal(ActivityContext parentContext, ActivityContextFactory<DomainActivityContextFinal> contextFactory) {
        super(parentContext, contextFactory);
    }

}
