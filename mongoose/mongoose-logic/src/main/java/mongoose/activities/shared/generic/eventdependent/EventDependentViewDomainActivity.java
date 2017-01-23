package mongoose.activities.shared.generic.eventdependent;

import naga.framework.activity.view.impl.ViewActivityImpl;
import naga.framework.activity.combinations.viewdomain.impl.ViewDomainActivityContextFinal;

/**
 * @author Bruno Salmon
 */
public abstract class EventDependentViewDomainActivity

    extends ViewActivityImpl
    implements EventDependentMixin<ViewDomainActivityContextFinal> {

}
