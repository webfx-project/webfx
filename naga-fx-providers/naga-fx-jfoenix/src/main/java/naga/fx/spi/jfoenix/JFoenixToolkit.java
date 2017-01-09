package naga.fx.spi.jfoenix;

import naga.fx.scene.Scene;
import naga.fx.naga.tk.ScenePeer;
import naga.fx.spi.javafx.JavaFxToolkit;
import naga.fx.spi.javafx.fx.FxScenePeer;
import naga.fx.spi.javafx.fx.stage.FxStagePeer;
import naga.fx.stage.Stage;

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

    @Override
    protected FxStagePeer createFxStagePeer(Stage stage, javafx.stage.Stage fxStage) {
        return new JFXStagePeer(stage, fxStage);
    }

    @Override
    public ScenePeer createScenePeer(Scene scene) {
        return new FxScenePeer(scene, JFXNodeViewerFactory.SINGLETON);
    }
}
