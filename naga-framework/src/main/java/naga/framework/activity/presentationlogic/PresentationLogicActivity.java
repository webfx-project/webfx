package naga.framework.activity.presentationlogic;

import naga.framework.activity.uiroute.UiRouteActivity;

/**
 * @author Bruno Salmon
 */
public interface PresentationLogicActivity
        <C extends PresentationLogicActivityContext<C, PM>, PM>

        extends UiRouteActivity<C> {
}
