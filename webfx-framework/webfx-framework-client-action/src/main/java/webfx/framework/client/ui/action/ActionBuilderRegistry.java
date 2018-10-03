package webfx.framework.client.ui.action;

import webfx.framework.client.ui.action.impl.ActionBuilderRegistryImpl;

/**
 * @author Bruno Salmon
 */
public interface ActionBuilderRegistry extends ActionFactory {

    @Override
    ActionBuilder newActionBuilder(Object actionKey);

    void registerActionBuilder(ActionBuilder actionBuilder);

    static ActionBuilderRegistry get() {
        return ActionBuilderRegistryImpl.get();
    }
}
