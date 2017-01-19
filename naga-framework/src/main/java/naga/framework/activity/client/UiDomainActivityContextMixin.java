package naga.framework.activity.client;

import naga.framework.activity.DomainActivityContextMixin;

/**
 * @author Bruno Salmon
 */
public interface UiDomainActivityContextMixin
        <C extends UiDomainActivityContext<C>>

        extends UiDomainActivityContext<C>,
        UiActivityContextMixin<C>,
        DomainActivityContextMixin<C> {
}
