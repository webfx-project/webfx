package naga.framework.ui.action;

import naga.framework.ui.action.impl.ActionRegistryImpl;
import naga.framework.ui.i18n.I18n;

/**
 * @author Bruno Salmon
 */
public interface ActionRegistry extends ActionProducer {

    void registerActionBuilder(ActionBuilder actionBuilder);

    void setI18n(I18n i18n);

    static ActionRegistry get() {
        return ActionRegistryImpl.get();
    }
}
