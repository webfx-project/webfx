package naga.framework.activity.combinations.viewdomain;

import naga.framework.activity.domain.DomainActivityContextMixin;
import naga.framework.activity.view.ViewActivityContextMixin;

/**
 * @author Bruno Salmon
 */
public interface ViewDomainActivityContextMixin
        <C extends ViewDomainActivityContext<C>>

        extends ViewDomainActivityContext<C>,
        ViewActivityContextMixin<C>,
        DomainActivityContextMixin<C> {
}
