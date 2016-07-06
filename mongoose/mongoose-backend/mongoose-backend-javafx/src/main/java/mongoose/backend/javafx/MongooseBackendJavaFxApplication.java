package mongoose.backend.javafx;

import javafx.scene.text.Font;
import mongoose.application.MongooseBackendApplication;
import naga.core.spi.toolkit.javafx.JavaFxToolkit;

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
        // Setting JavaFx start hook to load fonts
        JavaFxToolkit.setStartHook(() -> {
            try {
                double fontSize = 11;
                Class applicationClass = MongooseBackendJavaFxApplication.class;
                Font.loadFont(applicationClass.getResource("/fonts/OpenSans-Regular.ttf").toExternalForm(), fontSize);
                Font.loadFont(applicationClass.getResource("/fonts/OpenSans-Bold.ttf").toExternalForm(), fontSize);
                Font.loadFont(applicationClass.getResource("/fonts/OpenSans-Italic.ttf").toExternalForm(), fontSize);
                Font.loadFont(applicationClass.getResource("/fonts/OpenSans-BoldItalic.ttf").toExternalForm(), fontSize);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        // Setting JavaFx scene hook to apply the mongoose css file
        JavaFxToolkit.setSceneHook(scene -> scene.getStylesheets().addAll("css/mongoose.css"));
    }
}
