package naga.framework.activity.client;

import naga.framework.activity.DomainActivityContextDirectAccess;

/**
 * @author Bruno Salmon
 */
public interface UiDomainActivityContextDirectAccess<C extends UiDomainActivityContext<C>> extends UiDomainActivityContext<C>, UiActivityContextDirectAccess<C>, DomainActivityContextDirectAccess<C> {
}
