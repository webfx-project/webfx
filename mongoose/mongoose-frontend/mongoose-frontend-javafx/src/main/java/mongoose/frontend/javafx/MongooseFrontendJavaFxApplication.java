package mongoose.frontend.javafx;

import javafx.application.Application;
import javafx.stage.Stage;
import mongoose.logic.MongooseBackendApplication;
import naga.core.spi.gui.javafx.JavaFxToolkit;

/**
 * @author Bruno Salmon
 */
public class MongooseFrontendJavaFxApplication extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        JavaFxToolkit.get().start(primaryStage);
        MongooseBackendApplication.main(null);
    }

}
