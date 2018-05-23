package naga.framework.ui.action;

import naga.framework.services.i18n.spi.HasI18nProvider;

/**
 * @author Bruno Salmon
 */
public interface ActionFactoryMixin extends ActionFactory, HasI18nProvider {

    default ActionBuilder newActionBuilder(Object actionKey) {
        return getActionFactory().newActionBuilder(actionKey).setI18n(getI18n());
    }

    default ActionFactory getActionFactory() {
        return ActionBuilderRegistry.get();
    }

}
