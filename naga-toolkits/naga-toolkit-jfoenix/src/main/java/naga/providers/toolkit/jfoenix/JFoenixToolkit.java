package naga.providers.toolkit.jfoenix;

import naga.providers.toolkit.javafx.JavaFxToolkit;
import naga.providers.toolkit.javafx.fx.FxDrawing;
import naga.providers.toolkit.javafx.fx.FxDrawingNode;
import naga.providers.toolkit.jfoenix.nodes.layouts.JFoenixWindow;
import naga.toolkit.fx.spi.DrawingNode;

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
        registerNodeFactory(DrawingNode.class, () -> new FxDrawingNode() {
            @Override
            protected FxDrawing createDrawing() {
                return new FxDrawing(this, JFoenixNodeViewerFactory.SINGLETON);
            }
        });
    }
}
