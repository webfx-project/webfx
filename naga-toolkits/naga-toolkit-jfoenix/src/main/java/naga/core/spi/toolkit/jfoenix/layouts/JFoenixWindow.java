package naga.core.spi.toolkit.jfoenix.layouts;

import com.jfoenix.controls.JFXDecorator;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import naga.core.spi.toolkit.javafx.layouts.FxWindow;
import naga.core.spi.platform.Platform;

/**
 * @author Bruno Salmon
 */
public class JFoenixWindow extends FxWindow {

    public JFoenixWindow(Stage stage) {
        super(stage);
    }

    @Override
    protected Scene createScene(Parent root, double width, double height) {
        JFXDecorator decorator = new JFXDecorator(stage, root);
        decorator.setOnCloseButtonAction(() ->  stage.getOnCloseRequest().handle(null));
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
