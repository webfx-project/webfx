package webfx.framework.ui.graphic.controls.dialog;

import javafx.scene.control.Button;
import javafx.scene.layout.Region;
import webfx.platforms.core.util.function.Consumer;
import webfx.framework.ui.graphic.controls.button.ButtonFactoryMixin;

/**
 * @author Bruno Salmon
 */
public interface DialogBuilder extends ButtonFactoryMixin {

    Region build();

    void setDialogCallback(DialogCallback dialogCallback);

    DialogCallback getDialogCallback();

    default Button newButton(Object i18nKey, Consumer<DialogCallback> dialogAction) {
        return newButton(i18nKey, () -> dialogAction.accept(getDialogCallback()));
    }
}
