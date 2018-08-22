package naga.framework.activity.base.combinations.viewdomain;

import naga.framework.activity.base.elementals.domain.DomainActivityContext;
import naga.framework.activity.base.elementals.view.ViewActivityContext;
import naga.framework.activity.base.combinations.viewdomain.impl.ViewDomainActivityContextFinal;
import naga.framework.activity.ActivityContext;

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
