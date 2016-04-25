package mongoose.frontend.javafx;

import javafx.application.Application;
import javafx.stage.Stage;
import mongoose.logic.MongooseLogic;
import naga.core.spi.gui.javafx.JavaFxToolkit;

/**
 * @author Bruno Salmon
 */
public class MongooseFrontendJavaFxApplication extends Application {

    static {
        MongooseLogic.setUpWebSocketConnection();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        JavaFxToolkit.get().start(primaryStage);

        MongooseLogic.runFrontendApplication();
    }

}
