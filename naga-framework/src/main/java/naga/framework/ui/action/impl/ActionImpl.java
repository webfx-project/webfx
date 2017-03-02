package naga.framework.ui.action.impl;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import naga.framework.ui.action.Action;

/**
 * @author Bruno Salmon
 */
public class ActionImpl implements Action {

    private final Object i18nKey;
    private final Object iconUrlOrJson;
    private final EventHandler<ActionEvent> handler;

    public ActionImpl(Object i18nKey, Object iconUrlOrJson, EventHandler<ActionEvent> handler) {
        this.i18nKey = i18nKey;
        this.iconUrlOrJson = iconUrlOrJson;
        this.handler = handler;
    }

    @Override
    public Object getI18nKey() {
        return i18nKey;
    }

    @Override
    public Object getIconUrlOrJson() {
        return iconUrlOrJson;
    }

    @Override
    public EventHandler<ActionEvent> getHandler() {
        return handler;
    }
}
