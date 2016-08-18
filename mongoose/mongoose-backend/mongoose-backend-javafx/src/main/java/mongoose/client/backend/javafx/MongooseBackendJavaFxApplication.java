package mongoose.client.backend.javafx;

import mongoose.client.backend.MongooseBackendApplication;
import naga.providers.toolkit.javafx.JavaFxToolkit;

/**
 * @author Bruno Salmon
 */
public class MongooseBackendJavaFxApplication {

    public static void main(String[] args) {
        installJavaFxHooks();
        // Once hooks are set, we can start the application
        MongooseBackendApplication.main(args);
    }

    public static void installJavaFxHooks() {
        // Setting JavaFx scene hook to apply the mongoose css file
        JavaFxToolkit.setSceneHook(scene -> scene.getStylesheets().addAll("mongoose/client/backend/javafx/css/mongoose.css"));
    }
}
