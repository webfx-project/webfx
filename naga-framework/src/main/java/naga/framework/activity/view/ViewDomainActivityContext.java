package naga.framework.activity.view;

import naga.framework.activity.DomainActivityContext;
import naga.platform.activity.ActivityContext;

/**
 * @author Bruno Salmon
 */
public interface ViewDomainActivityContext
        <THIS extends ViewDomainActivityContext<THIS>>

        extends ViewActivityContext<THIS>,
        DomainActivityContext<THIS> {

    static ViewDomainActivityContextFinal create(ActivityContext parentContext) {
        return new ViewDomainActivityContextFinal(parentContext, ViewDomainActivityContext::create);
    }
}
