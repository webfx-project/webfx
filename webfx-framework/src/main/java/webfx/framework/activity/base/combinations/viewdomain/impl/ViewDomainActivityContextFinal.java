package webfx.framework.activity.base.combinations.viewdomain.impl;

import webfx.framework.activity.ActivityContext;

/**
 * @author Bruno Salmon
 */
public final class ViewDomainActivityContextFinal extends ViewDomainActivityContextBase<ViewDomainActivityContextFinal> {

    public ViewDomainActivityContextFinal(ActivityContext parentContext) {
        super(parentContext, ViewDomainActivityContextFinal::new);
    }
}
