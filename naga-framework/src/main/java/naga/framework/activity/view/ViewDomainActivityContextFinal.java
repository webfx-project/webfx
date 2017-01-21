package naga.framework.activity.view;

import naga.platform.activity.ActivityContext;
import naga.platform.activity.ActivityContextFactory;

/**
 * @author Bruno Salmon
 */
public final class ViewDomainActivityContextFinal extends ViewDomainActivityContextBase<ViewDomainActivityContextFinal> {

    ViewDomainActivityContextFinal(ActivityContext parentContext, ActivityContextFactory<ViewDomainActivityContextFinal> contextFactory) {
        super(parentContext, contextFactory);
    }
}
