package mongoose.client.backend.javafx;

import javafx.scene.text.Font;
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
        // Setting JavaFx start hook to load mongoose.application.backend.javafx.fonts
        JavaFxToolkit.setStartHook(() -> {
            try {
                double fontSize = 11;
                Class applicationClass = MongooseBackendJavaFxApplication.class;
                Font.loadFont(applicationClass.getResource("/mongoose/client/backend/javafx/fonts/OpenSans-Regular.ttf").toExternalForm(), fontSize);
                Font.loadFont(applicationClass.getResource("/mongoose/client/backend/javafx/fonts/OpenSans-Bold.ttf").toExternalForm(), fontSize);
                Font.loadFont(applicationClass.getResource("/mongoose/client/backend/javafx/fonts/OpenSans-Italic.ttf").toExternalForm(), fontSize);
                Font.loadFont(applicationClass.getResource("/mongoose/client/backend/javafx/fonts/OpenSans-BoldItalic.ttf").toExternalForm(), fontSize);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        // Setting JavaFx scene hook to apply the mongoose css file
        JavaFxToolkit.setSceneHook(scene -> scene.getStylesheets().addAll("mongoose/client/backend/javafx/css/mongoose.css"));
    }
}
