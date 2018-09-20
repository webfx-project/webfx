package webfx.framework.ui.controls;

import javafx.scene.control.Control;
import javafx.scene.control.Skin;
import webfx.fxkits.core.util.properties.Properties;
import webfx.platforms.core.util.function.Consumer;

/**
 * @author Bruno Salmon
 */
public final class Controls {

    public static void onSkinReady(Control control, Runnable runnable) {
        onSkinReady(control, skin -> runnable.run());
    }

    public static void onSkinReady(Control control, Consumer<Skin<?>> consumer) {
        Properties.onPropertySet(control.skinProperty(), consumer);
    }
}
