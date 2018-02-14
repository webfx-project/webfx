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
    private static ActionRegistryImpl INSTANCE = new ActionRegistryImpl();

    public static ActionRegistryImpl get() {
        return INSTANCE;
    }

    @Override
    public void setI18n(I18n i18n) {
        this.i18n = i18n;
    }

    @Override
    public I18n getI8n() {
        return i18n;
    }

    @Override
    public void registerAction(ActionBuilder actionBuilder) {
        actionBuilders.put(actionBuilder.getActionKey(), actionBuilder);
    }

    @Override
    public ActionBuilder newActionBuilder(Object actionKey) {
        ActionBuilder actionBuilder = actionBuilders.get(actionKey);
        if (actionBuilder == null)
            actionBuilder = new ActionBuilder(actionKey).setI18nKey(actionKey).register();
        return actionBuilder.duplicate();
    }

}
