package naga.framework.activity.combinations.viewdomain.impl;

import naga.platform.activity.ActivityContext;

/**
 * @author Bruno Salmon
 */
public final class ViewDomainActivityContextFinal extends ViewDomainActivityContextBase<ViewDomainActivityContextFinal> {

    public ViewDomainActivityContextFinal(ActivityContext parentContext) {
        super(parentContext, ViewDomainActivityContextFinal::new);
    }
}
