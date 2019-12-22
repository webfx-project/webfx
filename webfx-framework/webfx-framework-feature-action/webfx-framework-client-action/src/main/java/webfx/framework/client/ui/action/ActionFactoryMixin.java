package webfx.framework.client.ui.action;

/**
 * @author Bruno Salmon
 */
public interface ActionFactoryMixin extends ActionFactory {

    default ActionBuilder newActionBuilder(Object actionKey) {
        return getActionFactory().newActionBuilder(actionKey);
    }

    default ActionFactory getActionFactory() {
        return ActionBuilderRegistry.get();
    }

}
