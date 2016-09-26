package naga.providers.toolkit.jfoenix;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import naga.providers.toolkit.javafx.JavaFxToolkit;
import naga.providers.toolkit.javafx.nodes.controls.FxButton;
import naga.providers.toolkit.javafx.nodes.controls.FxCheckBox;
import naga.providers.toolkit.jfoenix.nodes.layouts.JFoenixWindow;
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
        registerNodeFactoryAndWrapper(CheckBox.class, () -> new FxCheckBox(new JFXCheckBox()), JFXCheckBox.class,FxCheckBox::new);
        registerNodeFactoryAndWrapper(Button.class, () -> new FxButton(new JFXButton()), JFXButton.class, FxButton::new);
        //registerNodeFactory(ToggleSwitch.class, () -> new FxToggleSwitch(new JFXToggleButton()));
    }
}
