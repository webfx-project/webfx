package webfx.framework.client.ui.controls;

import javafx.scene.control.Control;
import javafx.scene.control.Skin;
import webfx.kit.util.properties.Properties;
import java.util.function.Consumer;

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
