package naga.fx.spi.jfoenix;

import com.jfoenix.controls.JFXDecorator;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 * @author Bruno Salmon
 */
public class JFXStagePeer extends FxStagePeer {

    JFXStagePeer(naga.fx.stage.Stage stage, Stage fxStage) {
        super(stage, fxStage);
    }

    private StackPane foenixStackPane;

    @Override
    protected Scene createFxScene(Parent root, double width, double height) {
        JFXDecorator decorator = new JFXDecorator(fxStage, root);
        ((Region) decorator.getChildren().get(0)).setBackground(new Background(new BackgroundFill(Color.LIGHTBLUE, CornerRadii.EMPTY, Insets.EMPTY)));
        foenixStackPane = (StackPane) decorator.getChildren().get(1);
        decorator.setOnCloseButtonAction(() -> fxStage.getOnCloseRequest().handle(null));
        Scene scene = super.createFxScene(decorator, width, height);
        try {
            scene.getStylesheets().addAll(
                    getClass().getResource("/resources/css/jfoenix-fonts.css").toExternalForm(),
                    getClass().getResource("/resources/css/jfoenix-design.css").toExternalForm(),
                    //getClass().getResource("/css/jfoenix-components.css").toExternalForm()
                    getClass().getResource("/css/jfoenix-main-demo.css").toExternalForm()
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
        return scene;
    }

    @Override
    protected double stagePaddingWidth() {
        return 4;
    }

    @Override
    protected double stagePaddingHeight() {
        return 8;
    }

    @Override
    protected void setFxSceneRoot(Scene fxScene, Parent fxRoot) {
        foenixStackPane.getChildren().setAll(fxRoot);
    }

    @Override
    protected void setFxRootNow(Parent fxRoot) {
        if (foenixStackPane != null)
            foenixStackPane.getChildren().setAll(fxRoot);
        else
            super.setFxRootNow(fxRoot);
    }

}
