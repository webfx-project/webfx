package naga.framework.ui.graphic.controls;

import javafx.scene.control.Control;
import javafx.scene.control.Skin;
import naga.fx.properties.Properties;
import naga.util.function.Consumer;

/**
 * @author Bruno Salmon
 */
public class Controls {

    public static void onSkinReady(Control control, Runnable runnable) {
        onSkinReady(control, skin -> runnable.run());
    }

    public static void onSkinReady(Control control, Consumer<Skin<?>> consumer) {
        Properties.onPropertySet(control.skinProperty(), consumer);
    }
}
