package naga.framework.activity.view.impl;

import naga.framework.activity.combinations.viewdomain.ViewDomainActivityContextMixin;
import naga.framework.activity.combinations.viewdomain.impl.ViewDomainActivityContextFinal;

/**
 * @author Bruno Salmon
 */
public abstract class ViewActivityImpl extends ViewActivityBase<ViewDomainActivityContextFinal>
    implements ViewDomainActivityContextMixin<ViewDomainActivityContextFinal> {
}
