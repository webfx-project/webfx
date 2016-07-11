package naga.framework.ui.activity;

import naga.platform.activity.ActivityContext;

/**
 * @author Bruno Salmon
 */
public interface UiDomainActivityContext<C extends UiDomainActivityContext<C>> extends UiActivityContext<C>, DomainActivityContext<C> {

    static UiDomainActivityContext create(ActivityContext parentContext) {
        return new UiDomainActivityContextImpl(parentContext, UiDomainActivityContext::create);
    }
}
