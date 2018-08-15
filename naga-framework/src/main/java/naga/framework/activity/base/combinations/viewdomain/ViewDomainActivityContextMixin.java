package naga.framework.activity.base.combinations.viewdomain;

import naga.framework.activity.base.elementals.domain.DomainActivityContextMixin;
import naga.framework.activity.base.elementals.view.ViewActivityContextMixin;

/**
 * @author Bruno Salmon
 */
public interface ViewDomainActivityContextMixin
        <C extends ViewDomainActivityContext<C>>

        extends ViewDomainActivityContext<C>,
        ViewActivityContextMixin<C>,
        DomainActivityContextMixin<C> {
}
