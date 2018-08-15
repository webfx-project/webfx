package naga.framework.activity.base.elementals.presentation.logic;

import naga.framework.activity.base.elementals.activeproperty.ActivePropertyActivity;

/**
 * @author Bruno Salmon
 */
public interface PresentationLogicActivity
        <C extends PresentationLogicActivityContext<C, PM>, PM>

        extends ActivePropertyActivity<C> {
}
