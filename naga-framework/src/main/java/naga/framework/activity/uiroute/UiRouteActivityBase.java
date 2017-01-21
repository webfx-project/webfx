package naga.framework.activity.uiroute;

import javafx.beans.property.Property;
import naga.platform.activity.ActivityBase;

/**
 * @author Bruno Salmon
 */
public class UiRouteActivityBase
        <C extends UiRouteActivityContext<C>>

        extends ActivityBase<C>
        implements UiRouteActivity<C>,
        UiRouteActivityContextMixin<C> {

    @Override
    protected void setActive(boolean active) {
        super.setActive(active);
        ((Property<Boolean>) activeProperty()).setValue(active);
    }

}
