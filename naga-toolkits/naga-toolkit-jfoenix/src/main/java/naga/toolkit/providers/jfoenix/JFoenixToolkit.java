package naga.toolkit.providers.jfoenix;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import naga.toolkit.providers.javafx.JavaFxToolkit;
import naga.toolkit.providers.javafx.nodes.controls.FxButton;
import naga.toolkit.providers.javafx.nodes.controls.FxCheckBox;
import naga.toolkit.providers.jfoenix.nodes.layouts.JFoenixWindow;
import naga.toolkit.spi.nodes.controls.Button;
import naga.toolkit.spi.nodes.controls.CheckBox;

/**
 * @author Bruno Salmon
 */
public class JFoenixToolkit extends JavaFxToolkit {

    /*static {
        new Thread(()->{
            try {
                SVGGlyphLoader.loadGlyphsFont(JFoenixToolkit.class.getResourceAsStream("/resources/fonts/icomoon.svg"),"icomoon.svg");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }*/

    public JFoenixToolkit() {
        super(() -> JavaFxToolkit.FxApplication.applicationWindow = new JFoenixWindow(JavaFxToolkit.FxApplication.primaryStage));
        registerNodeFactory(CheckBox.class, () -> new FxCheckBox(new JFXCheckBox()));
        registerNodeFactory(Button.class, () -> new FxButton(new JFXButton()));
        //registerNodeFactory(ToggleSwitch.class, () -> new FxToggleSwitch(new JFXToggleButton()));
    }
}
