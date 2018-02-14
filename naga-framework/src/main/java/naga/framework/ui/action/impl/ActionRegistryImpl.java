package naga.framework.ui.action.impl;

import naga.framework.ui.action.ActionBuilder;
import naga.framework.ui.action.ActionRegistry;
import naga.framework.ui.i18n.I18n;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Bruno Salmon
 */
public class ActionRegistryImpl implements ActionRegistry {

    private I18n i18n;
    private final Map<Object, ActionBuilder> actionBuilders = new HashMap<>();
    private final static ActionRegistryImpl INSTANCE = new ActionRegistryImpl();

    public static ActionRegistryImpl get() {
        return INSTANCE;
    }

    @Override
    public void registerActionBuilder(ActionBuilder actionBuilder) {
        actionBuilders.put(actionBuilder.getActionKey(), actionBuilder.setActionRegistry(this));
    }

    @Override
    public void setI18n(I18n i18n) {
        this.i18n = i18n;
    }

    @Override
    public ActionBuilder newActionBuilder(Object actionKey) {
        ActionBuilder actionBuilder = actionBuilders.get(actionKey);
        if (actionBuilder == null)
            registerActionBuilder(actionBuilder = new ActionBuilder(actionKey).setI18nKey(actionKey));
        actionBuilder = actionBuilder.duplicate();
        if (actionBuilder.getI18n() == null)
            actionBuilder.setI18n(i18n);
        return actionBuilder;
    }

}
