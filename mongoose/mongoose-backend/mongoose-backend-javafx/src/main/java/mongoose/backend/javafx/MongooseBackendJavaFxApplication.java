package mongoose.backend.javafx;

import javafx.application.Application;
import javafx.stage.Stage;
import mongoose.logic.MongooseLogic;
import naga.core.spi.gui.javafx.FxToolkit;

/**
 * @author Bruno Salmon
 */
public class MongooseBackendJavaFxApplication extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        FxToolkit.get().start(primaryStage);

        MongooseLogic.runBackendApplication();
    }
}
