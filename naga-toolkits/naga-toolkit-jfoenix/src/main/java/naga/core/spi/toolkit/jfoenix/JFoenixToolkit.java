package naga.core.spi.toolkit.jfoenix;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import naga.core.spi.toolkit.javafx.JavaFxToolkit;
import naga.core.spi.toolkit.javafx.controls.FxButton;
import naga.core.spi.toolkit.javafx.controls.FxCheckBox;
import naga.core.spi.toolkit.jfoenix.layouts.JFoenixWindow;
import naga.core.spi.toolkit.controls.Button;
import naga.core.spi.toolkit.controls.CheckBox;

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
