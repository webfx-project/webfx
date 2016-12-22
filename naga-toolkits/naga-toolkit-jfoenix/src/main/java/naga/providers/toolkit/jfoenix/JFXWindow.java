package naga.providers.toolkit.jfoenix;

import com.jfoenix.controls.JFXDecorator;
import javafx.beans.value.ObservableDoubleValue;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import naga.providers.toolkit.javafx.fx.stage.FxWindow;

/**
 * @author Bruno Salmon
 */
public class JFXWindow extends FxWindow {

    JFXWindow(Stage stage) {
        super(stage);
    }

    private StackPane foenixStackPane;
    private ObservableDoubleValue fitContentWidthProperty;
    private ObservableDoubleValue fitContentHeightProperty;

    @Override
    protected Scene createScene(Parent root, double width, double height) {
        JFXDecorator decorator = new JFXDecorator(stage, root);
        ((Region) decorator.getChildren().get(0)).setBackground(new Background(new BackgroundFill(Color.LIGHTBLUE, CornerRadii.EMPTY, Insets.EMPTY)));
        foenixStackPane = (StackPane) decorator.getChildren().get(1);
        decorator.setOnCloseButtonAction(() ->  stage.getOnCloseRequest().handle(null));
        Scene scene = super.createScene(decorator, width, height);
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
        fitContentWidthProperty = foenixStackPane.widthProperty().subtract(8);
        fitContentHeightProperty = foenixStackPane.heightProperty().subtract(8);
        return scene;
    }

    @Override
    protected void setSceneRoot(Scene scene, Parent root) {
        foenixStackPane.getChildren().setAll(root);
    }

    @Override
    protected void setWindowContent(Parent rootComponent) {
        if (foenixStackPane != null)
            foenixStackPane.getChildren().setAll(rootComponent);
        else
            super.setWindowContent(rootComponent);
    }

    @Override
    protected ObservableDoubleValue getFitContentWidthProperty() {
        return fitContentWidthProperty;
    }

    @Override
    protected ObservableDoubleValue getFitContentHeightProperty() {
        return fitContentHeightProperty;
    }
}
