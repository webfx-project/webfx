package naga.framework.ui.action.impl;

import naga.framework.ui.action.ActionBuilder;
import naga.framework.ui.action.ActionBuilderRegistry;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Bruno Salmon
 */
public class ActionBuilderRegistryImpl implements ActionBuilderRegistry {

    private final static ActionBuilderRegistryImpl INSTANCE = new ActionBuilderRegistryImpl();

    private final Map<Object /* actionKey */, ActionBuilder> actionBuilders = new HashMap<>();

    public static ActionBuilderRegistryImpl get() {
        return INSTANCE;
    }

    @Override
    public void registerActionBuilder(ActionBuilder actionBuilder) {
        actionBuilders.put(actionBuilder.getActionKey(), actionBuilder.setRegistry(this));
    }

    @Override
    public ActionBuilder newActionBuilder(Object actionKey) {
        ActionBuilder actionBuilder = actionBuilders.get(actionKey);
        if (actionBuilder == null)
            registerActionBuilder(actionBuilder = new ActionBuilder(actionKey).setI18nKey(actionKey));
        return actionBuilder.duplicate();
    }

}
