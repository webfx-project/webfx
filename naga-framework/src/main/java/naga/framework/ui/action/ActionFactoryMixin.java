package naga.framework.ui.action;

import naga.framework.ui.i18n.HasI18n;

/**
 * @author Bruno Salmon
 */
public interface ActionFactoryMixin extends ActionFactory, HasI18n {

    default ActionBuilder newActionBuilder(Object actionKey) {
        return getActionFactory().newActionBuilder(actionKey).setI18n(getI18n());
    }

    default ActionFactory getActionFactory() {
        return ActionBuilderRegistry.get();
    }

}
