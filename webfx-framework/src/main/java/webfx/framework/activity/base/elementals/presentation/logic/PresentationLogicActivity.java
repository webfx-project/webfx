package webfx.framework.activity.base.elementals.presentation.logic;

import webfx.framework.activity.base.elementals.activeproperty.ActivePropertyActivity;

/**
 * @author Bruno Salmon
 */
public interface PresentationLogicActivity
        <C extends PresentationLogicActivityContext<C, PM>, PM>

        extends ActivePropertyActivity<C> {
}
