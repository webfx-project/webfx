package naga.framework.ui.action;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import naga.framework.ui.action.impl.ActionImpl;
import naga.framework.ui.graphic.controls.button.ButtonUtil;
import naga.framework.ui.i18n.I18n;

/**
 * @author Bruno Salmon
 */
public interface Action {

    Object getI18nKey();

    Object getIconUrlOrJson();

    EventHandler<ActionEvent> getHandler();

    static Action create(Object i18nKey, Object iconUrlOrJson, EventHandler<ActionEvent> handler) {
        return new ActionImpl(i18nKey, iconUrlOrJson, handler);
    }

    default Button toButton(I18n i18n) {
        return ButtonUtil.newButton(this, i18n);
    }

}
