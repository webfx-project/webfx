package naga.providers.toolkit.jfoenix;

import naga.providers.toolkit.javafx.JavaFxToolkit;

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
        super(() -> new JFoenixWindow(JavaFxToolkit.FxApplication.primaryStage), JFoenixNodeViewerFactory.SINGLETON);
    }
}
