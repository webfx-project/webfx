package naga.framework.ui.action;

import naga.framework.ui.action.impl.ActionBuilderRegistryImpl;
import naga.framework.services.i18n.spi.I18nProvider;

/**
 * @author Bruno Salmon
 */
public interface ActionBuilderRegistry extends ActionFactory {

    @Override
    ActionBuilder newActionBuilder(Object actionKey);

    void registerActionBuilder(ActionBuilder actionBuilder);

    void setI18n(I18nProvider i18n);

    static ActionBuilderRegistry get() {
        return ActionBuilderRegistryImpl.get();
    }
}
