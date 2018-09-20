package webfx.framework.activity.impl.elementals.presentation.logic;

import webfx.framework.activity.impl.elementals.activeproperty.ActivePropertyActivity;

/**
 * @author Bruno Salmon
 */
public interface PresentationLogicActivity
        <C extends PresentationLogicActivityContext<C, PM>, PM>

        extends ActivePropertyActivity<C> {
}
