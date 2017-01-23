package naga.framework.activity.presentation.logic;

/**
 * @author Bruno Salmon
 */
public interface PresentationLogicActivity
        <C extends PresentationLogicActivityContext<C, PM>, PM>

        extends naga.framework.activity.activeproperty.ActivePropertyActivity<C> {
}
