package naga.framework.activity.client;

import naga.framework.activity.DomainActivityContext;
import naga.platform.activity.ActivityContext;

/**
 * @author Bruno Salmon
 */
public interface UiDomainActivityContext
        <THIS extends UiDomainActivityContext<THIS>>

        extends UiActivityContext<THIS>,
        DomainActivityContext<THIS> {

    static UiDomainActivityContextFinal create(ActivityContext parentContext) {
        return new UiDomainActivityContextFinal(parentContext, UiDomainActivityContext::create);
    }
}
