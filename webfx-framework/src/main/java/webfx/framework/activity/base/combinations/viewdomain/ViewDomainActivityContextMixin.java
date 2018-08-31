package webfx.framework.activity.base.combinations.viewdomain;

import webfx.framework.activity.base.elementals.domain.DomainActivityContextMixin;
import webfx.framework.activity.base.elementals.view.ViewActivityContextMixin;

/**
 * @author Bruno Salmon
 */
public interface ViewDomainActivityContextMixin
        <C extends ViewDomainActivityContext<C>>

        extends ViewDomainActivityContext<C>,
        ViewActivityContextMixin<C>,
        DomainActivityContextMixin<C> {
}
