package naga.core.spi.gui.jfoenix;

import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXDecorator;
import javafx.scene.Parent;
import javafx.scene.Scene;
import naga.core.spi.gui.javafx.FxToolkit;
import naga.core.spi.gui.javafx.nodes.FxCheckBox;
import naga.core.spi.gui.nodes.CheckBox;
import naga.core.spi.platform.Platform;

/**
 * @author Bruno Salmon
 */
public class JFoenixToolkit extends FxToolkit {

    /*static {
        new Thread(()->{
            try {
                SVGGlyphLoader.loadGlyphsFont(JFoenixToolkit.class.getResourceAsStream("/resources/fonts/icomoon.svg"),"icomoon.svg");
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }).start();
    }*/

    public JFoenixToolkit() {
        registerNodeFactory(CheckBox.class, () -> new FxCheckBox(new JFXCheckBox()));
        //registerNodeFactory(ToggleSwitch.class, () -> new FxToggleSwitch(new JFXToggleButton()));
    }

    @Override
    protected Scene createScene(Parent root, double width, double height) {
        JFXDecorator decorator = new JFXDecorator(primaryStage, root);
        decorator.setOnCloseButtonAction(() ->  primaryStage.getOnCloseRequest().handle(null));
        Scene scene = super.createScene(decorator, width, height);
        try {
            scene.getStylesheets().addAll(
                    getClass().getResource("/resources/css/jfoenix-fonts.css").toExternalForm(),
                    getClass().getResource("/resources/css/jfoenix-design.css").toExternalForm(),
                    //getClass().getResource("/css/jfoenix-components.css").toExternalForm(),
                    getClass().getResource("/css/jfoenix-main-demo.css").toExternalForm()
            );
        } catch (Exception e) {
            Platform.log(e);
        }
        return scene;
    }
}
