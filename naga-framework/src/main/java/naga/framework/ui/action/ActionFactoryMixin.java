package naga.framework.ui.action;

import naga.framework.ui.i18n.HasI18n;

/**
 * @author Bruno Salmon
 */
public interface ActionFactoryMixin extends ActionProducer, HasI18n {

    default ActionRegistry getActionRegistry() {
        return ActionRegistry.get();
    }

    default ActionBuilder newActionBuilder(Object actionKey) {
        return getActionRegistry().newActionBuilder(actionKey).setI18n(getI18n());
    }

}
