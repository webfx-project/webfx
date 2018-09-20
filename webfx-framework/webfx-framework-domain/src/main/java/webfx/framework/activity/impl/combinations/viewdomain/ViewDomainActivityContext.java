package webfx.framework.activity.impl.combinations.viewdomain;

import webfx.framework.activity.impl.elementals.domain.DomainActivityContext;
import webfx.framework.activity.impl.elementals.view.ViewActivityContext;
import webfx.framework.activity.impl.combinations.viewdomain.impl.ViewDomainActivityContextFinal;
import webfx.framework.activity.ActivityContext;

/**
 * @author Bruno Salmon
 */
public interface ViewDomainActivityContext
        <THIS extends ViewDomainActivityContext<THIS>>

        extends ViewActivityContext<THIS>,
        DomainActivityContext<THIS> {

    static ViewDomainActivityContextFinal create(ActivityContext parentContext) {
        return new ViewDomainActivityContextFinal(parentContext);
    }
}
