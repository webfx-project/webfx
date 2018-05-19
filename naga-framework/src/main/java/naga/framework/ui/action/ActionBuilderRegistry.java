package naga.framework.ui.action;

import naga.framework.ui.action.impl.ActionBuilderRegistryImpl;
import naga.framework.ui.i18n.I18n;

/**
 * @author Bruno Salmon
 */
public interface ActionBuilderRegistry extends ActionFactory {

    @Override
    ActionBuilder newActionBuilder(Object actionKey);

    void registerActionBuilder(ActionBuilder actionBuilder);

    void setI18n(I18n i18n);

    static ActionBuilderRegistry get() {
        return ActionBuilderRegistryImpl.get();
    }
}
