package naga.core.spi.gui.jfoenix;

import com.jfoenix.controls.JFXCheckBox;
import naga.core.spi.gui.javafx.JavaFxToolkit;
import naga.core.spi.gui.javafx.nodes.FxCheckBox;
import naga.core.spi.gui.jfoenix.nodes.JFoenixWindow;
import naga.core.spi.gui.nodes.CheckBox;

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
        //registerNodeFactory(ToggleSwitch.class, () -> new FxToggleSwitch(new JFXToggleButton()));
    }
}
